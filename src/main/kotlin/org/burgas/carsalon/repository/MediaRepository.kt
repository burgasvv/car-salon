package org.burgas.carsalon.repository

import org.burgas.carsalon.entity.media.Media
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MediaRepository : JpaRepository<Media, UUID>