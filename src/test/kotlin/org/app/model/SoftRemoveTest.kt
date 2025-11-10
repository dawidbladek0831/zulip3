package org.app.model

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.app.common.findById
import org.app.model.post.Post
import org.app.model.post.PostComment
import org.app.model.post.PostDetails
import org.app.model.post.PostTag
import org.app.model.tag.Tag
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
internal class SoftRemoveTest {

    @Inject
    lateinit var sf: Mutiny.SessionFactory


    @Nested
    inner class TagTests {
        @Test
        fun shouldUpdateTagInsteadOfDeleting() {
            db.insert(Tag.maximal())

            sf.withTransaction { session ->
                session.find(Tag::class.java, 1L)
                    .flatMap { session.remove(it) }
            }.await().indefinitely()

            sf.findById(Post::class, 1L).await().indefinitely()
            Assertions.assertEquals(db.count(Tag::class), 1L)
        }

    }

    @Nested
    inner class PostCommentTests {
        @Test
        fun shouldMarkPostCommentAsDeleted() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

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
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

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
    }


    @Nested
    inner class PostDetailsTests {
        @Test
        fun shouldMarkPostDetailsAsDeleted() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            sf.withTransaction { session ->
                session.find(PostDetails::class.java, 1L)
                    .flatMap { session.remove(it) }
            }.await().indefinitely()

            val result = sf.findById(PostDetails::class, 1L).await().indefinitely()
            Assertions.assertNull(result)
            Assertions.assertEquals(db.count(PostDetails::class), 1)

            val post = sf.findById(Post::class, 1L).await().indefinitely()
            Assertions.assertNull(post.details)
        }

        @Test
        fun shouldInsertPostDetailsAfterMarkingAsDeleted() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            sf.withTransaction { session ->
                session.find(PostDetails::class.java, 1L)
                    .flatMap { session.remove(it) }
            }.await().indefinitely()

            sf.withTransaction { session ->
                session.find(Post::class.java, 1L)
                    .flatMap { post ->
                        post.details = PostDetails("details2")
                        post.link()
                        session.persist(post)
                    }
            }.await().indefinitely()

            Assertions.assertEquals(2, db.count(PostDetails::class))

            val post = sf.findById(Post::class, 1L).await().indefinitely()
            Assertions.assertNotNull(post.details)
            Assertions.assertEquals(1L, post.details?.id)

            sf.findById(PostDetails::class, 1L).await().indefinitely()
            Assertions.assertNotNull(post.details)
        }

        @Test
        fun shouldMarkPostDetailsAsDeletedTwoTimes() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            sf.withTransaction { session ->
                session.find(PostDetails::class.java, 1L)
                    .flatMap { session.remove(it) }
            }.await().indefinitely()

            sf.withTransaction { session ->
                session.find(Post::class.java, 1L)
                    .flatMap { post ->
                        post.details = PostDetails("details2")
                        post.link()
                        session.persist(post)
                    }
            }.await().indefinitely()

            sf.withTransaction { session ->
                session.find(PostDetails::class.java, 1L)
                    .flatMap { session.remove(it) }
            }.await().indefinitely()

            val post = sf.findById(Post::class, 1L).await().indefinitely()
            Assertions.assertNull(post.details)

            Assertions.assertEquals(2, db.count(PostDetails::class))
        }
    }


    @Nested
    inner class PostTagTests {
        @Test
        fun shouldNotReturnTagsMarkedAsDeleted() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            sf.withTransaction { session ->
                session.find(Tag::class.java, 1L)
                    .flatMap { session.remove(it) }
            }.await().indefinitely()


            Assertions.assertEquals(1, db.count(Tag::class))
            val post = sf.findById(Post::class, 1L).await().indefinitely()
            Assertions.assertEquals(0, post.tags.size)
        }

        @Test
        fun shouldNotReturnTagsMarkedAsDeletedInJoinTable() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            sf.withTransaction { session ->
                session.find(Post::class.java, 1L)
                    .flatMap { post ->
                        post.tags.clear()
                        session.merge(post)
                    }
            }.await().indefinitely()


            Assertions.assertEquals(1, db.count("post_tag"))
            val post = sf.findById(Post::class, 1L).await().indefinitely()
            Assertions.assertEquals(0, post.tags.size)
        }
    }


    @Inject
    lateinit var db: DbData

    @BeforeEach
    fun beforeEach() {
        db.truncate(Tag::class)
        db.truncate(Post::class)
        db.truncate(PostComment::class)
        db.truncate(PostDetails::class)
        db.truncate(PostTag::class)
    }
}