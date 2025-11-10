package org.app.model.post

import jakarta.persistence.*
import org.app.model.tag.Tag
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@SQLDelete(sql = "UPDATE post_tag SET deleted = TRUE WHERE post_id = $1 AND tag_id = $2")
@SQLRestriction("deleted = FALSE")
@Entity
@Table(name = "post_tag")
@IdClass(PostTagId::class)
internal class PostTag(
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    val tag: Tag,

    val deleted: Boolean = false
) {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post? = null

    companion object
}

internal data class PostTagId(
    val post: Long? = null,
    val tag: Long? = null
)

