package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MediaDTO(
    val url: String,
    val contentType: String,
    val contentSize: Long,
    val description: String?,
    val altText: String,
    val caption: String?,
    val createdAt: String
)