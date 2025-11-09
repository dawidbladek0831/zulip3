package org.app.model

import org.app.model.post.Post
import org.app.model.post.PostComment
import org.app.model.post.PostDetails
import org.app.model.tag.Tag


internal fun Tag.Companion.maximal(): Tag =
    Tag("java")

internal fun PostComment.Companion.maximal(): PostComment =
    PostComment("comment1")

internal fun PostDetails.Companion.maximal(): PostDetails =
    PostDetails("details1")

internal fun Post.Companion.maximal(): Post = Post(
    name = "exceptions",
    comments = mutableListOf(
        PostComment("comment1"),
        PostComment("comment2"),
    ),
    tags = mutableSetOf(
    ),
    details = PostDetails("details1")
)