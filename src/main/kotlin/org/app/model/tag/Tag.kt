package org.app.model.tag

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "tag")
internal data class Tag(
    val name: String
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object
}
