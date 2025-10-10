package br.ufpr.tads.daily_iu_services.adapter.input.media.dto

data class MediaCreateDTO(
    val url: String,
    val contentType: String?,
    val contentSize: Long
)
