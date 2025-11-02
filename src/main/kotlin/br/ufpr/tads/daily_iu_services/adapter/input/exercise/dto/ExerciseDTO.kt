package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO

data class ExerciseDTO(
    val id: Long?,
    val title: String,
    val instructions: String,
    val category: String,
    val media: List<MediaDTO>,
    val benefits: List<ExerciseAttributeDTO>,
    val repetitions: Int,
    val sets: Int,
    val restTime: Int,
    val duration: Int
)
