package br.ufpr.tads.daily_iu_services.adapter.input.contact.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactDTO(
    @field:NotBlank
    @field:Email
    val userEmail: String,

    @field:NotBlank
    val subject: String,

    @field:NotBlank
    val text: String
)