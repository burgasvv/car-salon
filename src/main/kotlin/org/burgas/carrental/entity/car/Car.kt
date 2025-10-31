package org.burgas.carrental.entity.car

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.Table
import org.burgas.carrental.entity.BaseEntity
import org.burgas.carrental.entity.brand.Brand
import java.util.UUID

@Entity
@Table(name = "car", schema = "public")
@NamedEntityGraph(
    name = "car-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "brand")
    ]
)
class Car : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    var brand: Brand? = null

    @Column(name = "model", nullable = false)
    lateinit var model: String

    @Column(name = "characteristics", nullable = false)
    lateinit var characteristics: String

    @Column(name = "rent_price", nullable = false)
    var rentPrice: Double = 0.0

    constructor()

    @Suppress("unused")
    constructor(id: UUID, brand: Brand?, model: String, characteristics: String, rentPrice: Double) {
        this.id = id
        this.brand = brand
        this.model = model
        this.characteristics = characteristics
        this.rentPrice = rentPrice
    }
}