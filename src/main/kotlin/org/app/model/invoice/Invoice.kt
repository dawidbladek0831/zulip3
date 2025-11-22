package org.app.model.invoice

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "invoice")
@NamedEntityGraph(
    name = "Invoice.full",
    attributeNodes = [
        NamedAttributeNode("contracts"),
        NamedAttributeNode("lines", subgraph = "InvoiceLine.descriptions"),
        NamedAttributeNode("details"),
        NamedAttributeNode("payment")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "InvoiceLine.descriptions",
            attributeNodes = [
                NamedAttributeNode("descriptions")
            ]
        )
    ]
)
internal data class Invoice(
    val number: String,

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val contracts: MutableList<InvoiceContract> = mutableListOf(),

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val lines: MutableList<InvoiceLine> = mutableListOf(),

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], optional = false)
    val details: InvoiceDetails,

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val payment: InvoicePayment?,

    ) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    init {
        link()
    }

    fun link() {
        contracts.forEach { it.invoice = this }
        lines.forEach { it.invoice = this }
        details.invoice = this
        payment?.invoice = this
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
