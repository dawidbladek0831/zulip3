package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "post_comment")
internal data class PostComment(
    val content: String
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
