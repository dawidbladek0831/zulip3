package org.app.common.impl

import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.app.common.DbData
import org.app.common.executeOnVertex
import org.app.model.tag.Tag
import org.hibernate.reactive.mutiny.Mutiny
import kotlin.reflect.KClass

@ApplicationScoped
internal class PostgresDataManipulation(
    val sf: Mutiny.SessionFactory
) : DbData {
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

    override fun count(klass: KClass<*>): Long {
        val tableName = getTableName(klass)
        return count(tableName)
    }

    override fun count(tableName: String): Long {
        return executeOnVertex {
            sf.withStatelessTransaction { session ->
                session.createNativeQuery("SELECT COUNT(*) FROM $tableName", Long::class.java)
                    .singleResult
            }
        }
    }

    override fun <T : Any> findById(klass: KClass<T>, id: Long): T? {
        return executeOnVertex {
            sf.withSession { session ->
                val cb = session.criteriaBuilder
                val query = cb.createQuery(klass.java)
                val root = query.from(klass.java)
                val predicate = cb.equal(root.get<T>("id"), id)
                query.select(root).where(predicate)

                session.createQuery(query).singleResultOrNull
            }
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
}