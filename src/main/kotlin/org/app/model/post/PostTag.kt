package org.app.model.post

import jakarta.persistence.*
import org.app.model.tag.Tag


@Entity
@Table(name = "post_tag")
@IdClass(PostTagId::class)
internal class PostTag(
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    val tag: Tag
)  {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post? = null

    companion object
}

internal data class PostTagId(
    val post: Post? = null,
    val tag: Tag? = null
)

