package org.app.model.post

import org.app.model.tag.Tag
import org.app.model.tag.maximal

internal fun PostComment.Companion.maximal(
    id: Long? = null, content: String = "amazing"
): PostComment = PostComment(
    content = content
).apply { this.id = id }

internal fun PostDetails.Companion.maximal(
    id: Long? = null, name: String = "checked"
): PostDetails = PostDetails(
    name = name
).apply { this.id = id }

internal fun Post.Companion.maximal(
    id: Long? = null,
    name: String = "exceptions",
    details: PostDetails = PostDetails.maximal(),
    comments: MutableList<PostComment> = mutableListOf(PostComment.maximal(), PostComment.maximal()),
    tags: MutableSet<Tag> = mutableSetOf(Tag.maximal(id = 1L), Tag.maximal(id = 2L))
): Post = Post(
    name = name, details = details, comments = comments, tags = tags
).apply { this.id = id }