package br.ufpr.tads.daily_iu_services.adapter.input.media.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MediaDTO(
    val id: Long? = null,

    @field:NotBlank(message = "A URL não pode ser vazia")
    val url: String,

    @field:NotBlank(message = "O contentType não pode ser vazio")
    val contentType: String,

    @field:NotNull("O contentSize não pode ser nulo")
    val contentSize: Long,

    @field:NotBlank(message = "O texto alternativo não pode ser vazio")
    val altText: String,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime?
)