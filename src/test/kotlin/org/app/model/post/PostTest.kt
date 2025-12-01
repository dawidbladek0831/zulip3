package org.app.model.post

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.JoinType
import org.app.common.DbData
import org.app.model.tag.Tag
import org.app.model.tag.maximal
import org.hibernate.query.restriction.Path
import org.hibernate.query.restriction.Restriction
import org.hibernate.query.specification.SelectionSpecification
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
internal class PostTest {
    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Inject
    lateinit var em: EntityManager

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

    @Nested
    inner class FetchBySelectionSpecification {

//        SelectionSpecification
//        .create(InvoiceCrud::class.java)
//        .fetch(Path.from(InvoiceCrud::class.java).to(InvoiceCrud.invoiceBody))
//        .fetch(Path.from(InvoiceCrud::class.java).to(InvoiceCrud.priceSummary))
//        .fetch(Path.from(InvoiceCrud::class.java).to(InvoiceCrud.payment))
//        .fetch(Path.from(InvoiceCrud::class.java).to(InvoiceCrud.transactionTerms))
//        .augment { , , root ->
//            root.fetch(InvoiceCrud.transactionTerms, JoinType.LEFT)
//                .fetch(TransactionTermsCrud.orders, JoinType.LEFT)
//        }

        @Test
        fun `should return post by SelectionSpecification`() {
            db.insert(Tag.maximal())
            db.insert(Tag.maximal(name = "python"))
            db.insert(Post.maximal())

            val result = SelectionSpecification.create(Post::class.java)
                .restrict(Restriction.equal(Post_.id, 1L))
                .fetch(Path.from(Post::class.java).to(Post_.comments.name, PostComment::class.java))
                .fetch(Path.from(Post::class.java).to(Post_.tags.name, Tag::class.java))
                .fetch(Path.from(Post::class.java).to(Post_.details.name, PostDetails::class.java))
//                .augment { builder, query, root ->
//                    query.distinct(true)
//                    root.fetch(Post_.details, JoinType.LEFT)
//                    root.fetch(Post_.comments, JoinType.LEFT)
//                    root.fetch(Post_.tags, JoinType.LEFT)
//                }
                .createQuery(em)
                .singleResult

            em.detach(result)
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