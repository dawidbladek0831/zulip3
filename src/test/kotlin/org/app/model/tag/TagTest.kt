package org.app.model.tag

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
internal class TagTest {

    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Nested
    inner class Save {
        @Test
        fun `should save tag`() {
            sf.withTransaction { session -> session.persist(Tag.maximal()) }.await().indefinitely()

            assertEquals(1, db.count(Tag::class))
        }
    }

    @Nested
    inner class Fetch {
        @Test
        fun `should return tag`() {
            db.insert(Tag.maximal())

            val result = sf.withSession { session -> session.find(Tag::class.java, 1L) }.await().indefinitely()

            assertEquals("java", result.name)
        }

        @Test
        fun `should return null when tag does not exist`() {
            val result = sf.withSession { session -> session.find(Tag::class.java, 1L) }.await().indefinitely()

            assertNull(result)
        }
    }

    @Inject
    lateinit var db: DbData

    @BeforeEach
    fun beforeEach() {
        db.truncate(Tag::class)
    }
}