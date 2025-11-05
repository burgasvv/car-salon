package org.burgas.carrental.entity.media

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.burgas.carrental.entity.BaseEntity
import java.util.*

@Entity
@Table(name = "media", schema = "public")
class Media : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @Column(name = "name", nullable = false)
    lateinit var name: String

    @Column(name = "content_type", nullable = false)
    lateinit var contentType: String

    @Column(name = "format", nullable = false)
    lateinit var format: String

    @Column(name = "size", nullable = false)
    var size: Long = 0

    @JsonIgnore
    @Column(name = "data", nullable = false)
    var data: ByteArray = byteArrayOf()

    constructor()
}