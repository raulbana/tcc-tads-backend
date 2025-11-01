package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.AuthorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO

data class ContentAdminDTO(
    val id: Long,
    val title: String,
    val description: String,
    val subtitle: String?,
    val subcontent: String?,
    val categories: List<String>,
    val author: AuthorDTO,
    val media: List<MediaDTO>,
    val reports: List<ContentReportAdminDTO>,
)
