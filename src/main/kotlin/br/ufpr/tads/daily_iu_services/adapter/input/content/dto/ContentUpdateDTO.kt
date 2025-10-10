package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class ContentUpdateDTO(

    @field:NotBlank(message = "O título não pode ser vazio")
    val title: String,

    @field:NotBlank(message = "A descrição não pode ser vazia")
    val description: String,

    val subtitle: String?,
    val subcontent: String?,

    val media: List<MediaDTO>
)