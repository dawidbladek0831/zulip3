package org.app.model.invoice

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "invoice_line")
internal data class InvoiceLine(
    var name: String,
    @OneToMany(mappedBy = "invoiceLine", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val descriptions: MutableList<InvoiceLineDescription> = mutableListOf()
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var invoice: Invoice? = null

    init {
        link()
    }

    fun link() {
        descriptions.forEach { it.invoiceLine = this }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
