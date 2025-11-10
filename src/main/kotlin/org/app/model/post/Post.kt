package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@SQLDelete(sql = "UPDATE post SET deleted = TRUE WHERE id = $1")
@SQLRestriction("deleted = FALSE")
@Entity
@Table(name = "post")
internal data class Post(
    val name: String,

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    val comments: MutableList<PostComment> = mutableListOf(),

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    val tags: MutableList<PostTag> = mutableListOf(),

    @OneToOne(mappedBy = "post", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    var details: PostDetails?,

    val deleted: Boolean = false
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null

    init {
        link()
    }

    fun link() {
        comments.forEach { it.post = this }
        details?.post = this
        details?.id = this.id
        tags.forEach { it.post = this }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
