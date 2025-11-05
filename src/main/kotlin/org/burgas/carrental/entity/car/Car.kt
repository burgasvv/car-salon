package org.burgas.carrental.entity.car

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.NamedSubgraph
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.burgas.carrental.entity.BaseEntity
import org.burgas.carrental.entity.brand.Brand
import org.burgas.carrental.entity.media.Media
import org.burgas.carrental.entity.rent.Rent
import java.util.UUID

@Entity
@Table(name = "car", schema = "public")
@NamedEntityGraph(
    name = "car-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "brand"),
        NamedAttributeNode(value = "media"),
        NamedAttributeNode(value = "rents", subgraph = "rents-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "rents-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "identity", subgraph = "identity-subgraph")
            ]
        ),
        NamedSubgraph(
            name = "identity-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "media")
            ]
        )
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

    @Column(name = "free", nullable = false)
    var free: Boolean = true

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "car_media",
        joinColumns = [JoinColumn(name = "car_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "media_id", referencedColumnName = "id")]
    )
    var media: MutableList<Media> = mutableListOf()

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    var rents: MutableList<Rent> = mutableListOf()

    constructor()
}