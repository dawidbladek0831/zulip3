package org.app.base

import org.hibernate.proxy.HibernateProxy

abstract class BaseEntity<ID> {

    abstract val id: ID?

    override fun equals(
        other: Any?
    ): Boolean {
        if (this === other) return true
        if (other == null) return false

        val thisClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass

        val otherClass =
            if (other is HibernateProxy) {
                other.hibernateLazyInitializer.persistentClass
            } else {
                other.javaClass
            }

        if (thisClass != otherClass) return false
        if (other !is BaseEntity<*>) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return if (this is HibernateProxy) {
            (this as HibernateProxy).hibernateLazyInitializer.persistentClass.hashCode()
        } else {
            javaClass.hashCode()
        }
    }
}
