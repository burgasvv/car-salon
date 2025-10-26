package org.burgas.carsalon.service

import jakarta.servlet.http.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.burgas.carsalon.entity.media.Media
import org.burgas.carsalon.exception.MediaNotFoundException
import org.burgas.carsalon.message.MediaMessages
import org.burgas.carsalon.repository.MediaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class MediaService : BaseService {

    private final val mediaRepository: MediaRepository

    constructor(mediaRepository: MediaRepository) {
        this.mediaRepository = mediaRepository
    }

    fun findEntity(mediaId: UUID): Media {
        return this.mediaRepository.findById(mediaId)
            .orElseThrow { throw MediaNotFoundException(MediaMessages.MEDIA_NOT_FOUND.message) }
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    fun create(part: Part): Media {
        val media = Media().apply {
            this.name = part.name
            this.contentType = part.contentType
            this.format = part.contentType.split("/").last()
            this.size = part.size
            this.data = part.inputStream.readAllBytes()
        }
        return this.mediaRepository.save(media)
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    fun change(mediaId: UUID, part: Part): Media {
        val media = findEntity(mediaId).apply {
            this.name = part.name
            this.contentType = part.contentType
            this.format = part.contentType.split("/").last()
            this.size = part.size
            this.data = part.inputStream.readAllBytes()
        }
        return this.mediaRepository.save(media)
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    fun delete(mediaId: UUID) {
        val media = findEntity(mediaId)
        this.mediaRepository.delete(media)
    }
}