package org.app.model.person

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "person")
internal data class Person(
    val name: String,

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], optional = false)
    var address: PersonAddress,

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var profile: PersonProfile?

) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    init {
        link()
    }

    fun link() {
        address.person = this
        profile?.person = this
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
