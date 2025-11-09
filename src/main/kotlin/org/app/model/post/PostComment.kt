package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@SQLDelete(sql = "UPDATE post_comment SET deleted = TRUE WHERE id = $1")
@SQLRestriction("deleted = FALSE")
@Entity
@Table(name = "post_comment")
internal data class PostComment(
    val content: String,

    val deleted: Boolean = false
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
