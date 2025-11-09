package org.app.model.tag

import jakarta.persistence.*
import org.app.base.BaseEntity

@Entity
@Table(name = "tag")
internal class Tag(
    val name: String
) : BaseEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null

    companion object
}
