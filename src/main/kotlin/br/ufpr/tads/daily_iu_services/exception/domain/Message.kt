package br.ufpr.tads.daily_iu_services.exception.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Message (
    val code: Int,
    val origin: String,
    val message: String,
    val details: List<String>? = null
)