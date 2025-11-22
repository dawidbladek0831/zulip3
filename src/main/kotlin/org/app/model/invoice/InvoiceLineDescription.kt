package org.app.model.invoice

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "invoice_line_description")
internal data class InvoiceLineDescription(
    var description: String
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var invoiceLine: InvoiceLine? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
