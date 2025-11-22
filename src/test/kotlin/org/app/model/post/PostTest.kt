package org.app.model.post

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.app.model.tag.Tag
import org.app.model.tag.maximal
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
internal class PostTest {
    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Nested
    inner class Save {
        @Test
        fun `should save post`() {
            db.insert(Tag.maximal())
            db.insert(Tag.maximal(name = "python"))

            sf.withTransaction { session -> session.persist(Post.maximal()) }.await().indefinitely()

            assertEquals(1, db.count(Post::class))
            assertEquals(1, db.count(PostDetails::class))
            assertEquals(2, db.count(PostComment::class))
            assertEquals(2, db.count("post_tag"))
        }
    }

    @Nested
    inner class FetchByMutinyFetch {
        @Test
        fun `should return post`() {
            db.insert(Tag.maximal())
            db.insert(Tag.maximal(name = "python"))
            db.insert(Post.maximal())

            val result = sf.withSession { session ->
                session
                    .find(Post::class.java, 1L)
                    .call { post -> Mutiny.fetch(post.comments) }
                    .call { post -> Mutiny.fetch(post.tags) }
                    .call { post -> Mutiny.fetch(post.details) }
            }.await().indefinitely()

            assertEquals("exceptions", result.name)
            assertEquals(2, result.comments.size)
            assertEquals("amazing", result.comments.first().content)
            assertEquals(2, result.tags.size)
            assertEquals("java", result.tags.first().name)
            assertEquals("checked", result.details.name)
        }
    }


    @Nested
    inner class FetchByGraph {
        @Test
        fun `should return post by dynamic graph`() {
            db.insert(Tag.maximal())
            db.insert(Tag.maximal(name = "python"))
            db.insert(Post.maximal())

            val result = sf.withSession { session ->
                val graph = session.createEntityGraph(Post::class.java)
                graph.addElementSubgraph<PostComment>("comments")
                graph.addElementSubgraph<Tag>("tags")
                graph.addSubgraph<PostDetails>("details")
                session.find(graph, 1L)
            }.await().indefinitely()

            assertEquals("exceptions", result.name)
            assertEquals(2, result.comments.size)
            assertEquals("amazing", result.comments.first().content)
            assertEquals(2, result.tags.size)
            assertEquals("java", result.tags.first().name)
            assertEquals("checked", result.details.name)
        }

        @Test
        fun `should return post by named graph`() {
            db.insert(Tag.maximal())
            db.insert(Tag.maximal(name = "python"))
            db.insert(Post.maximal())

            val result = sf.withSession { session ->
                session.find(
                    session.getEntityGraph(Post::class.java, "Post.full"),
                    1L
                )
            }.await().indefinitely()

            assertEquals("exceptions", result.name)
            assertEquals(2, result.comments.size)
            assertEquals("amazing", result.comments.first().content)
            assertEquals(2, result.tags.size)
            assertEquals("java", result.tags.first().name)
            assertEquals("checked", result.details.name)
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
    }
}