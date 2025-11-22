package org.app.model.person

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.app.model.post.PostDetails
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
internal class PersonTest {
    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Nested
    inner class Save {
        @Test
        fun `should save person`() {
            sf.withTransaction { session -> session.persist(Person.maximal()) }.await().indefinitely()

            Assertions.assertEquals(1, db.count(Person::class))
            Assertions.assertEquals(1, db.count(PersonAddress::class))
            Assertions.assertEquals(1, db.count(PersonProfile::class))
        }
    }

    /*
       * does not work when optional = true. Probably only with Hibernate Reactive
       * */
    @Nested
    inner class FetchByMutinyFetch {
//        @Test
//        fun `should return person`() {
//            db.insert(Person.maximal())
//
//            val result = sf.withSession { session -> session
//                .find(Person::class.java, 1L)
//                .call { person -> Mutiny.fetch(person.address) }
//                .call { person -> Mutiny.fetch(person.profile) }
//            }.await().indefinitely()
//
//            Assertions.assertEquals("Ala", result.name)
//            Assertions.assertEquals("Wall street", result.address.street)
//            Assertions.assertEquals("Alli", result.profile?.nickname)
//        }
    }


    @Nested
    inner class FetchByGraph {
        @Test
        fun `should return person by dynamic graph`() {
            db.insert(Person.maximal())

            val result = sf.withSession { session ->
                val graph = session.createEntityGraph(Person::class.java)
                graph.addSubgraph<PostDetails>("profile")
                graph.addSubgraph<PostDetails>("address")
                session.find(graph, 1L)
            }.await().indefinitely()

            Assertions.assertEquals("Ala", result.name)
            Assertions.assertEquals("Wall street", result.address.street)
            Assertions.assertEquals("Alli", result.profile?.nickname)
        }

        @Test
        fun `should return person by dynamic graph without profile`() {
            db.insert(
                Person.maximal(
                    profile = null,
                )
            )

            val result = sf.withSession { session ->
                val graph = session.createEntityGraph(Person::class.java)
                graph.addSubgraph<PostDetails>("address")
                graph.addSubgraph<PostDetails>("profile")
                session.find(graph, 1L)
            }.await().indefinitely()

            Assertions.assertEquals("Ala", result.name)
            Assertions.assertEquals("Wall street", result.address.street)
            Assertions.assertEquals(null, result.profile)
        }
    }

    @Nested
    inner class Remove {
        @Test
        fun `should remove person profile`() {
            db.insert(Person.maximal())

            sf.withSession { session ->
                val graph = session.createEntityGraph(Person::class.java)
                graph.addSubgraph<PostDetails>("profile")
                graph.addSubgraph<PostDetails>("address")
                session.find(graph, 1L)
                    .flatMap { person ->
                        person.profile?.person = null
                        person.profile = null
                        session.merge(person)
                    }
                    .flatMap { session.flush() }
            }.await().indefinitely()

            val result = sf.withSession { session ->
                val graph = session.createEntityGraph(Person::class.java)
                graph.addSubgraph<PostDetails>("profile")
                graph.addSubgraph<PostDetails>("address")
                session.find(graph, 1L)
            }.await().indefinitely()

            Assertions.assertEquals("Ala", result.name)
            Assertions.assertEquals("Wall street", result.address.street)
            Assertions.assertEquals(null, result.profile)
        }
    }

    @Inject
    lateinit var db: DbData

    @BeforeEach
    fun beforeEach() {
        db.truncate(Person::class)
        db.truncate(PersonAddress::class)
        db.truncate(PersonProfile::class)
    }
}