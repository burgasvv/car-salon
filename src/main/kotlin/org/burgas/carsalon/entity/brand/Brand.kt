package org.burgas.carsalon.entity.brand

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.burgas.carsalon.entity.BaseEntity
import org.burgas.carsalon.entity.car.Car
import java.util.UUID

@Entity
@Table(name = "brand", schema = "public")
@NamedEntityGraph(
    name = "brand-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "cars")
    ]
)
class Brand : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @Column(name = "name", nullable = false)
    lateinit var name: String

    @Column(name = "description", nullable = false)
    lateinit var description: String

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var cars: MutableList<Car> = mutableListOf()

    constructor()

    @Suppress("unused")
    constructor(id: UUID, name: String, description: String, cars: MutableList<Car>) {
        this.id = id
        this.name = name
        this.description = description
        this.cars = cars
    }
}