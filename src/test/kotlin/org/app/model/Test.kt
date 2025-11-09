package org.app.model

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.app.model.post.Post
import org.app.model.post.PostComment
import org.app.model.post.PostDetails
import org.app.model.tag.Tag
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
internal class Test {

    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Test
    fun shouldSaveTag() {
        sf.withTransaction { session -> session.persist(Tag.maximal()) }.await().indefinitely()

        val result = sf.withSession { session -> session.find(Tag::class.java, 1L) }.await().indefinitely()

        Assertions.assertEquals(result.id, 1L)
    }

    @Test
    fun shouldSavePost() {
        sf.withTransaction { session -> session.persist(Post.maximal()) }.await().indefinitely()

        val result = sf.withSession { session -> session.find(Post::class.java, 1L) }.await().indefinitely()

        Assertions.assertEquals(result.id, 1L)
        Assertions.assertEquals(result.comments.size, 2)
        Assertions.assertNotNull(result.details)
    }

    @Inject
    lateinit var db: DbData

    @BeforeEach
    fun beforeEach() {
        db.truncate(Tag::class)
        db.truncate(Post::class)
        db.truncate(PostComment::class)
        db.truncate(PostDetails::class)
    }
}