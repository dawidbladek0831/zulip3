package org.app.model

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.app.model.post.Post
import org.app.model.post.PostComment
import org.app.model.post.PostDetails
import org.app.model.post.PostTag
import org.app.model.tag.Tag
import org.app.query.post.PostQuery
import org.app.query.post.PostTagQuery
import org.app.query.tag.TagQuery
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
internal class QueryTest {

    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Nested
    inner class TagSessionFind {
        @Test
        fun `should return tag`() {
            db.insert(Tag.maximal())
            val result = sf.withSession { session -> session.find(TagQuery::class.java, 1L) }.await().indefinitely()
            Assertions.assertNotNull(result)
            Assertions.assertEquals(1L, result.id)
        }

        @Test
        fun `should return null when tag does not exist`() {
            val result = sf.withSession { session -> session.find(TagQuery::class.java, 1L) }.await().indefinitely()
            Assertions.assertNull(result)
        }
    }

    @Nested
    inner class PostGraph {
        @Test
        fun `should return post by dynamic graph`() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            val result = sf.withSession { session ->
                val graph = session.createEntityGraph(PostQuery::class.java)
                graph.addSubgraph<PostDetails>("details")
                graph.addSubgraph<PostComment>("comments")
                val tagsSubgraph = graph.addSubgraph<PostTagQuery>("tags")
                tagsSubgraph.addAttributeNodes("tag")

                session.find(graph, 1L)
            }.await().indefinitely()

            Assertions.assertNotNull(result)
            Assertions.assertEquals(1L, result.id)
            Assertions.assertNotNull(result.details)
            Assertions.assertEquals(2, result.comments.size)
            Assertions.assertEquals(1, result.tags.size)
            Assertions.assertEquals("java", result.tags[0].tag.name)
        }

        @Test
        fun `should return post by named graph`() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            val result = sf.withSession { session ->
                session.find(
                    session.getEntityGraph(PostQuery::class.java, "PostQuery.full"),
                    1L
                )
            }.await().indefinitely()

            Assertions.assertNotNull(result)
            Assertions.assertEquals(1L, result.id)
            Assertions.assertNotNull(result.details)
            Assertions.assertEquals(2, result.comments.size)
            Assertions.assertEquals(1, result.tags.size)
            Assertions.assertEquals("java", result.tags[0].tag.name)
        }
    }

    @Nested
    inner class PostMutinyFetch {
        @Test
        fun `should return post`() {
            db.insert(Tag.maximal())
            db.insert(Post.maximal())

            val result = sf.withSession { session ->
                session.find(PostQuery::class.java, 1L)
                    .call { post -> Mutiny.fetch(post.details) }
                    .call { post -> Mutiny.fetch(post.comments) }
                    .call { post -> Mutiny.fetch(post.tags) }
            }.await().indefinitely()

            Assertions.assertNotNull(result)
            Assertions.assertEquals(1L, result.id)
            Assertions.assertNotNull(result.details)
            Assertions.assertEquals(2, result.comments.size)
            Assertions.assertEquals(1, result.tags.size)
            Assertions.assertEquals("java", result.tags[0].tag.name)
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