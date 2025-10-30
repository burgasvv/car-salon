package org.burgas.carsalon.entity.identity

import jakarta.persistence.*
import org.burgas.carsalon.entity.BaseEntity
import org.burgas.carsalon.entity.media.Media
import java.util.*

@Entity
@Table(name = "identity", schema = "public")
@NamedEntityGraph(
    name = "identity-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "media")
    ]
)
class Identity : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    lateinit var authority: Authority

    @Column(name = "email", unique = true, nullable = false)
    lateinit var email: String

    @Column(name = "password", nullable = false)
    lateinit var password: String

    @Column(name = "enabled", nullable = false)
    var enabled: Boolean = true

    @Column(name = "firstname", nullable = false)
    lateinit var firstname: String

    @Column(name = "lastname", nullable = false)
    lateinit var lastname: String

    @Column(name = "patronymic", nullable = false)
    lateinit var patronymic: String

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "identity_media",
        joinColumns = [
            JoinColumn(name = "identity_id", referencedColumnName = "id")
        ],
        inverseJoinColumns = [
            JoinColumn(name = "media_id", referencedColumnName = "id")
        ]
    )
    var media: MutableList<Media> = mutableListOf()

    constructor()
}