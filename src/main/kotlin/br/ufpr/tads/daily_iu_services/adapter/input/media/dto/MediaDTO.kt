package br.ufpr.tads.daily_iu_services.adapter.input.media.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MediaDTO(

    @field:NotBlank(message = "A URL não pode ser vazia")
    val url: String,

    @field:NotBlank(message = "O contentType não pode ser vazio")
    val contentType: String,

    @field:NotNull("O contentSize não pode ser nulo")
    val contentSize: Long,

    @field:NotBlank(message = "O texto alternativo não pode ser vazio")
    val altText: String,
    val createdAt: String?
)