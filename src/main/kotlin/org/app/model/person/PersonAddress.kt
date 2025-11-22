package org.app.model.person

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "person_address")
internal data class PersonAddress(
    val street: String,
) : BaseEntity<Long>() {
    @Id
    override var id: Long? = null

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY)
    var person: Person? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
