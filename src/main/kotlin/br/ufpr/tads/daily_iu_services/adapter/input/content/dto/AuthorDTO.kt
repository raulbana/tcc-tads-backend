package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthorDTO(
    val id: Long,
    val name: String,
    val profilePicture: String?,
)