package org.app.model

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.app.common.findById
import org.app.model.post.Post
import org.app.model.post.PostComment
import org.app.model.tag.Tag
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
internal class SoftRemoveTest {

    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Test
    fun shouldSaveTag() {
        sf.withTransaction { session -> session.persist(Tag.maximal()) }.await().indefinitely()

        val result = db.findById(Tag::class, id = 1L)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(result?.id, 1L)
    }

    @Test
    fun shouldUpdateTagInsteadOfDeleting() {
        sf.withTransaction { session -> session.persist(Tag.maximal()) }.await().indefinitely()

        sf.withTransaction { session ->
            session.find(Tag::class.java, 1L)
                .flatMap { session.remove(it) }
        }.await().indefinitely()

        val result = sf.findById(Post::class, 1L).await().indefinitely()
        Assertions.assertEquals(db.count(Tag::class), 1L)
    }

    @Test
    fun shouldMarkPostCommentAsDeleted() {
        sf.withTransaction { session -> session.persist(Post.maximal()) }.await().indefinitely()

        sf.withTransaction { session ->
            session.find(PostComment::class.java, 1L)
                .flatMap { session.remove(it) }
        }.await().indefinitely()

        val result = sf.findById(Post::class, 1L).await().indefinitely()
        Assertions.assertEquals(result.comments.size, 1)
        Assertions.assertEquals(db.count(Post::class), 1L)
        Assertions.assertEquals(db.count(PostComment::class), 2L)
    }

    @Test
    fun shouldMarkPostAndCommentsAsDeleted() {
        sf.withTransaction { session -> session.persist(Post.maximal()) }.await().indefinitely()

        sf.withTransaction { session ->
            session.find(Post::class.java, 1L)
                .flatMap { session.remove(it) }
        }.await().indefinitely()

        val result = sf.findById(Post::class, 1L).await().indefinitely()
        Assertions.assertNull(result)
        val postComment1 = sf.findById(PostComment::class, 1L).await().indefinitely()
        Assertions.assertNull(postComment1)
        val postComment2 = sf.findById(PostComment::class, 2L).await().indefinitely()
        Assertions.assertNull(postComment2)
        Assertions.assertEquals(db.count(Post::class), 1L)
        Assertions.assertEquals(db.count(PostComment::class), 2L)
    }

    @Inject
    lateinit var db: DbData

    @BeforeEach
    fun beforeEach() {
        db.truncate(Tag::class)
        db.truncate(Post::class)
        db.truncate(PostComment::class)
    }
}