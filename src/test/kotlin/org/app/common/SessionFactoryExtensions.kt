package org.app.common

import io.smallrye.mutiny.Uni
import jakarta.persistence.criteria.CriteriaDelete
import org.hibernate.reactive.mutiny.Mutiny
import kotlin.reflect.KClass


fun <T : Any, V> Mutiny.SessionFactory.removeBy(
    entityClass: KClass<T>,
    attributeName: String,
    value: V
): Uni<Int> {
    return withTransaction { session ->
        val cb = session.criteriaBuilder
        val deleteQuery = cb.createCriteriaDelete(entityClass.java) as CriteriaDelete<T>
        val root = deleteQuery.from(entityClass.java)
        val predicate = cb.equal(root.get<V>(attributeName), value)
        session.createMutationQuery(deleteQuery.where(predicate)).executeUpdate()
    }
}