package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.app.model.tag.Tag

@Entity
@Table(name = "post")
@NamedEntityGraph(
    name = "Post.full",
    attributeNodes = [
        NamedAttributeNode("comments"),
        NamedAttributeNode("tags"),
        NamedAttributeNode("details")
    ]
)
internal data class Post(
    val name: String,

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val comments: MutableList<PostComment> = mutableListOf(),

    @ManyToMany
    @JoinTable(name = "post_tag", joinColumns = [JoinColumn(name = "post_id")], inverseJoinColumns = [JoinColumn(name = "tag_id")])
    val tags: MutableSet<Tag> = mutableSetOf(),

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], optional = false)
    val details: PostDetails
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    init {
        link()
    }

    fun link() {
        comments.forEach { it.post = this }
        details.post = this
        details.id = this.id
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
