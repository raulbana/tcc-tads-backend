package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ContentSimpleDTO(
    val id: Long?,
    val title: String,
    val categories: List<String>,
    val author: AuthorDTO,
    val cover: MediaDTO?,
    val section: List<String>?,
    val isReposted: Boolean,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime
)