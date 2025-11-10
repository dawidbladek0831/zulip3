package org.app.query.tag

import jakarta.persistence.*
import org.app.base.BaseEntity
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.SQLRestriction

@SQLRestriction("deleted = FALSE")
@Entity
@Immutable
@Table(name = "tag")
internal data class TagQuery(
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
