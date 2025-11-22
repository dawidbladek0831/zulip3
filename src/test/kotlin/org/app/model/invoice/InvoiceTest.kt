package org.app.model.invoice

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.app.common.DbData
import org.hibernate.reactive.mutiny.Mutiny
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
internal class InvoiceTest {
    @Inject
    lateinit var sf: Mutiny.SessionFactory

    @Nested
    inner class Save {
        @Test
        fun `should save invoice`() {
            sf.withTransaction { session -> session.persist(Invoice.maximal()) }.await().indefinitely()

            Assertions.assertEquals(1, db.count(Invoice::class))
            Assertions.assertEquals(2, db.count(InvoiceContract::class))
            Assertions.assertEquals(1, db.count(InvoiceDetails::class))
            Assertions.assertEquals(2, db.count(InvoiceLine::class))
            Assertions.assertEquals(4, db.count(InvoiceLineDescription::class))
            Assertions.assertEquals(1, db.count(InvoicePayment::class))
        }
    }
    /*
    * Hibernate Reactive does not fetch nested collections via entity graph, when there is more than one collection on the same entity.....
    * */
//    @Nested
//    inner class FetchByGraph {
//        @Test
//        fun `should return invoice by dynamic graph`() {
//            db.insert(Invoice.maximal())
//
//            val result = sf.withSession { session ->
//                val graph = session.createEntityGraph(Invoice::class.java)
//                val lines = graph.addSubgraph("lines", InvoiceLine::class.java)
//                lines.addSubgraph("descriptions", InvoiceLineDescription::class.java)
//
//                graph.addSubgraph("contracts", InvoiceContract::class.java)
//                graph.addSubgraph("details", InvoiceDetails::class.java)
//                graph.addSubgraph("payment", InvoicePayment::class.java)
//                session.find(graph, 1L)
//            }.await().indefinitely()
//
//            assertEquals("Fa/2026/01/001", result.number)
//            assertEquals(2, result.lines.size)
//            assertEquals("book", result.lines.first().name)
//            assertEquals(2, result.lines.first().descriptions.size)
//            assertEquals("factory new", result.lines.first().descriptions.first().description)
//            assertEquals(2, result.contracts.size)
//            assertEquals("125203", result.contracts.first().number)
//            assertEquals(BigDecimal("100.00"), result.payment?.totalNet)
//            assertEquals(BigDecimal("123.00"), result.payment?.totalGross)
//            assertEquals( LocalDateTime.parse("2026-01-01T10:15:30"), result.details.issueDate)
//            assertEquals("New York", result.details.issuePlace)
//        }
//        @Test
//        fun `should return invoice by named graph`() {
//            db.insert(Invoice.maximal())
//
//            val result = sf.withSession { session ->
//                val graph = session.getEntityGraph(Invoice::class.java, "Invoice.full")
//                session.find(graph, 1L)
//            }.await().indefinitely()
//
//            assertEquals("Fa/2026/01/001", result.number)
//            assertEquals(2, result.lines.size)
//            assertEquals("book", result.lines.first().name)
//            assertEquals(2, result.lines.first().descriptions.size)
//            assertEquals("factory new", result.lines.first().descriptions.first().description)
//            assertEquals(2, result.contracts.size)
//            assertEquals("125203", result.contracts.first().number)
//            assertEquals(BigDecimal("100.00"), result.payment?.totalNet)
//            assertEquals(BigDecimal("123.00"), result.payment?.totalGross)
//            assertEquals( LocalDateTime.parse("2026-01-01T10:15:30"), result.details.issueDate)
//            assertEquals("New York", result.details.issuePlace)
//        }
//    }

    @Inject
    lateinit var db: DbData

    @BeforeEach
    fun beforeEach() {
        db.truncate(Invoice::class)
        db.truncate(InvoiceContract::class)
        db.truncate(InvoiceDetails::class)
        db.truncate(InvoiceLine::class)
        db.truncate(InvoiceLineDescription::class)
        db.truncate(InvoicePayment::class)
    }
}