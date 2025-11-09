package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "post_details")
internal class PostDetails(
    val name: String,
) : BaseEntity<Long>() {
    @Id
    override var id: Long? = null

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY)
    var post: Post? = null

    companion object
}
