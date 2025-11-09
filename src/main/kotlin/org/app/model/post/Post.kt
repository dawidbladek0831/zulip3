package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.app.model.tag.Tag

@Entity
@Table(name = "post")
internal class Post(
    val name: String,

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<PostComment> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(name = "post_tag", joinColumns = [JoinColumn(name = "post_id")], inverseJoinColumns = [JoinColumn(name = "tag_id")])
    val tags: MutableSet<Tag> = mutableSetOf()
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null

    init {
        linkRelations()
    }

    fun linkRelations() {
        comments.forEach { it.post = this }
    }

    companion object
}
