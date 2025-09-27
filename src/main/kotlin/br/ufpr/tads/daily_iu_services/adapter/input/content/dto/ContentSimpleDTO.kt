package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO

data class ContentSimpleDTO(
    val id: Long?,
    val title: String,
    val category: String,
    val author: AuthorDTO,
    val cover: MediaDTO?,
    val isReposted: Boolean,
    val createdAt: String,
    val updatedAt: String
)