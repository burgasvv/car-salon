package org.burgas.carsalon.entity.brand

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.burgas.carsalon.entity.BaseEntity
import java.util.UUID

@Entity
@Table(name = "brand", schema = "public")
class Brand : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @Column(name = "name", nullable = false)
    lateinit var name: String

    @Column(name = "description", nullable = false)
    lateinit var description: String

    constructor()

    @Suppress("unused")
    constructor(id: UUID, name: String, description: String) {
        this.id = id
        this.name = name
        this.description = description
    }
}