package org.app.common.impl

import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.app.common.DbDataManipulation
import org.app.common.executeOnVertex
import org.hibernate.reactive.mutiny.Mutiny
import kotlin.reflect.KClass

@ApplicationScoped
internal class PostgresDataManipulation(
    val sf: Mutiny.SessionFactory
) : DbDataManipulation {
    override fun truncate(klass: KClass<*>) {
        val tableName = getTableName(klass)
        truncate(tableName)
    }

    override fun truncate(tableName: String) {
        executeOnVertex {
            sf.withStatelessTransaction { session ->
                session.createNativeQuery<Int>("TRUNCATE TABLE $tableName RESTART IDENTITY CASCADE").executeUpdate()
            }.replaceWithUnit()
        }
    }

    private fun getTableName(klass: KClass<*>): String {
        return when {
            klass.java.isAnnotationPresent(Table::class.java) -> {
                val table = klass.java.getAnnotation(Table::class.java)
                table.name.ifBlank { klass.java.simpleName }
            }

            klass.java.isAnnotationPresent(Entity::class.java) -> {
                klass.java.simpleName
            }

            else -> throw IllegalArgumentException("Class ${klass.java.name} is not a JPA entity")
        }
    }

    override fun <T : Any> insert(entity: T) {
        executeOnVertex {
            sf.withSession { session ->
                session.persist(entity)
                    .flatMap { session.flush() }
            }
        }
    }
}