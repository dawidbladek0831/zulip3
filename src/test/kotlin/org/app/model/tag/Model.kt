package org.app.model.tag

internal fun Tag.Companion.maximal(
    id: Long? = null, name: String = "java"
): Tag = Tag(
    name = name
).apply { this.id = id }

