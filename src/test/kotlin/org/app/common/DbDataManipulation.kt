package org.app.common

import kotlin.reflect.KClass

internal interface DbDataManipulation {
    fun truncate(tableName: String)
    fun truncate(klass: KClass<*>)
    fun <T : Any> insert(entity: T)
}