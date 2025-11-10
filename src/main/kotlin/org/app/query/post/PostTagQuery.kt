package org.app.query.post

import jakarta.persistence.*
import org.app.model.post.PostTagId
import org.app.query.tag.TagQuery
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.SQLRestriction

@SQLRestriction("deleted = FALSE")
@Entity
@Immutable
@Table(name = "post_tag")
@IdClass(PostTagId::class)
internal class PostTagQuery(
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    val tag: TagQuery
) {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: PostQuery? = null

    companion object
}
