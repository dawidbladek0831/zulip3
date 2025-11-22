package org.app.model.invoice

import java.math.BigDecimal
import java.time.LocalDateTime

internal fun InvoicePayment.Companion.maximal(
    id: Long? = null, totalNet: BigDecimal = BigDecimal("100.00"), totalGross: BigDecimal = BigDecimal("123.00"),
): InvoicePayment = InvoicePayment(
    totalNet = totalNet,
    totalGross = totalGross
).apply { this.id = id }

internal fun InvoiceDetails.Companion.maximal(
    id: Long? = null, issueDate: LocalDateTime = LocalDateTime.parse("2026-01-01T10:15:30"),
    issuePlace: String = "New York"
): InvoiceDetails = InvoiceDetails(
    issueDate = issueDate,
    issuePlace = issuePlace
).apply { this.id = id }

internal fun InvoiceContract.Companion.maximal(
    id: Long? = null, number: String = "125203"
): InvoiceContract = InvoiceContract(
    number = number
).apply { this.id = id }

internal fun InvoiceLineDescription.Companion.maximal(
    id: Long? = null, description: String = "factory new"
): InvoiceLineDescription = InvoiceLineDescription(
    description = description
).apply { this.id = id }

internal fun InvoiceLine.Companion.maximal(
    id: Long? = null,
    name: String = "book",
    descriptions: MutableList<InvoiceLineDescription> = mutableListOf(
        InvoiceLineDescription.maximal(),
        InvoiceLineDescription.maximal()
    )
): InvoiceLine = InvoiceLine(
    name = name, descriptions = descriptions
).apply { this.id = id }

internal fun Invoice.Companion.maximal(
    id: Long? = null,
    number: String = "Fa/2026/01/001",
    contracts: MutableList<InvoiceContract> = mutableListOf(
        InvoiceContract.maximal(),
        InvoiceContract.maximal(),
    ),
    lines: MutableList<InvoiceLine> = mutableListOf(
        InvoiceLine.maximal(),
        InvoiceLine.maximal(),
    ),
    details: InvoiceDetails = InvoiceDetails.maximal(),
    payment: InvoicePayment = InvoicePayment.maximal()
): Invoice = Invoice(
    number = number,
    contracts = contracts,
    lines = lines,
    details = details,
    payment = payment
).apply { this.id = id }