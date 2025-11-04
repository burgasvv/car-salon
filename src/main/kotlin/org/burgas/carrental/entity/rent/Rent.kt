package org.burgas.carrental.entity.rent

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
import jakarta.persistence.NamedSubgraph
import jakarta.persistence.Table
import org.burgas.carrental.entity.BaseEntity
import org.burgas.carrental.entity.car.Car
import org.burgas.carrental.entity.identity.Identity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "rent", schema = "public")
@NamedEntityGraph(
    name = "rent-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "identity", subgraph = "identity-subgraph"),
        NamedAttributeNode(value = "car", subgraph = "car-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "identity-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "media")
            ]
        ),
        NamedSubgraph(
            name = "car-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "brand"),
                NamedAttributeNode(value = "media")
            ]
        )
    ]
)
class Rent : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_id", referencedColumnName = "id")
    lateinit var identity: Identity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    lateinit var car: Car

    @Column(name = "price", nullable = false)
    var price: Double = 0.0

    @Column(name = "start_time", nullable = false)
    lateinit var startTime: LocalDateTime

    @Column(name = "end_time", nullable = false)
    lateinit var endTime: LocalDateTime

    @Column(name = "closed", nullable = false)
    var closed: Boolean = false

    constructor()
}