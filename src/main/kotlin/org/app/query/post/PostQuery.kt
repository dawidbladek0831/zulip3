package org.app.query.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "post")
@NamedEntityGraph(
    name = "PostQuery.full",
    attributeNodes = [
        NamedAttributeNode("comments"),
        NamedAttributeNode("tags", subgraph = "tags"),
        NamedAttributeNode("details")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "tags",
            attributeNodes = [NamedAttributeNode("tag")]
        )
    ]
)

internal data class PostQuery(
    val name: String,

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    val comments: MutableList<PostCommentQuery> = mutableListOf(),

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    val tags: MutableList<PostTagQuery> = mutableListOf(),

    @OneToOne(mappedBy = "post", fetch = FetchType.EAGER)
    val details: PostDetailsQuery
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
