package org.app.query.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.SQLRestriction

@SQLRestriction("deleted = FALSE")
@Entity
@Immutable
@Table(name = "post_comment")
internal data class PostCommentQuery(
    val content: String
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var post: PostQuery? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
