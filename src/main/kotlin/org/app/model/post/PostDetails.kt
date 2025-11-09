package org.app.model.post

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@SQLDelete(sql = "UPDATE post_details SET deleted = TRUE WHERE id = $1")
@SQLRestriction("deleted = FALSE")
@Entity
@Table(name = "post_details")
internal class PostDetails(
    val name: String,

    val deleted: Boolean = false
) : BaseEntity<Long>() {
    @Id
    override var id: Long? = null

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY)
    var post: Post? = null

    companion object
}
