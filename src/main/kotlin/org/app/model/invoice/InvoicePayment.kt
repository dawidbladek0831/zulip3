package org.app.model.invoice

import jakarta.persistence.*
import org.app.base.BaseEntity
import java.math.BigDecimal

@Entity
@Table(name = "invoice_payment")
internal data class InvoicePayment(
    val totalNet: BigDecimal,
    val totalGross: BigDecimal
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
