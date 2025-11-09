package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "post_details")
internal data class PostDetails(
    val name: String,
) : BaseEntity<Long>() {
    @Id
    override var id: Long? = null

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY)
    var post: Post? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
