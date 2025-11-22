package org.app.model.invoice

import jakarta.persistence.*
import org.app.base.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "invoice_details")
internal data class InvoiceDetails(
    val issueDate: LocalDateTime,
    val issuePlace: String
) : BaseEntity<Long>() {
    @Id
    override var id: Long? = null

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    var invoice: Invoice? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
