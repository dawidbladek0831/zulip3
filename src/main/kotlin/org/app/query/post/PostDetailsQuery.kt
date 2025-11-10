package org.app.query.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.SQLRestriction

@SQLRestriction("deleted = FALSE")
@Entity
@Immutable
@Table(name = "post_details")
internal data class PostDetailsQuery(
    val name: String,
) : BaseEntity<Long>() {
    @Id
    override var id: Long? = null

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY)
    var post: PostQuery? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
