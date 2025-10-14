package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import java.time.LocalDateTime

data class ContentSimpleDTO(
    val id: Long?,
    val title: String,
    val categories: List<String>,
    val author: AuthorDTO,
    val cover: MediaDTO?,
    val isReposted: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)