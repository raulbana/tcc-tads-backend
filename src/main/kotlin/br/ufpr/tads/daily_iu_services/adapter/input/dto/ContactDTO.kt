package br.ufpr.tads.daily_iu_services.adapter.input.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactDTO(
    @NotBlank
    @Email
    val userEmail: String,

    @NotBlank
    val subject: String,

    @NotBlank
    val text: String
)
