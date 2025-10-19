package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ExerciseCreatorDTO(
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val instructions: String,

    @field:NotNull
    val categoryId: Long,

    val media: List<MediaDTO>,
    val attributes: List<Long>,
    val repetitions: Int,
    val sets: Int,
    val restTime: Int,
    val duration: Int
)
