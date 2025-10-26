package org.burgas.carsalon.entity.identity

import jakarta.persistence.*
import org.burgas.carsalon.entity.BaseEntity
import org.burgas.carsalon.entity.media.Media
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "identity", schema = "public")
@NamedEntityGraph(
    name = "identity-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "media")
    ]
)
class Identity : BaseEntity, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    lateinit var authority: Authority

    @Column(name = "email", unique = true, nullable = false)
    lateinit var email: String

    @Column(name = "pass", nullable = false)
    lateinit var pass: String

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

    @Suppress("unused")
    constructor(
        id: UUID,
        authority: Authority,
        email: String,
        pass: String,
        enabled: Boolean,
        firstname: String,
        lastname: String,
        patronymic: String,
        media: MutableList<Media>
    ) {
        this.id = id
        this.authority = authority
        this.email = email
        this.pass = pass
        this.enabled = enabled
        this.firstname = firstname
        this.lastname = lastname
        this.patronymic = patronymic
        this.media = media
    }

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(authority)
    }

    override fun getPassword(): String? {
        return this.pass
    }

    override fun getUsername(): String? {
        return this.email
    }

    override fun isEnabled(): Boolean {
        return this.enabled || !super.isEnabled()
    }
}