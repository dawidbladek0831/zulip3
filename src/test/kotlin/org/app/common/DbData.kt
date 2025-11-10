package org.app.common

import kotlin.reflect.KClass

internal interface DbData {
    fun truncate(tableName: String)
    fun truncate(klass: KClass<*>)
    fun count(tableName: String): Long
    fun count(klass: KClass<*>): Long
    fun <T : Any> findById(klass: KClass<T>, id: Long): T?
    fun <T : Any> insert(entity: T)
}