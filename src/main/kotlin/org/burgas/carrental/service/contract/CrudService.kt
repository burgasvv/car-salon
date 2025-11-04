package org.burgas.carrental.service.contract

import org.burgas.carrental.dto.Request
import org.burgas.carrental.dto.Response
import org.burgas.carrental.entity.BaseEntity
import org.springframework.stereotype.Component
import java.util.UUID

@Component
interface CrudService<in R : Request, out E : BaseEntity, out S : Response, out F : Response> {

    fun findEntity(id: UUID): E

    fun findAll(): List<S>

    fun findById(id: UUID): F

    fun create(request: R): F

    fun update(request: R): F

    fun delete(id: UUID)
}