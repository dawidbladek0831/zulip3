package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "post_comment")
internal class PostComment(
    val content: String
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post? = null

    companion object
}
