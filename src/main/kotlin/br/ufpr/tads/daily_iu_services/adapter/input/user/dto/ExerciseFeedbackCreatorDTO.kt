package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import br.ufpr.tads.daily_iu_services.domain.validator.ValidDate
import org.jetbrains.annotations.NotNull
import java.time.LocalDate

data class ExerciseFeedbackCreatorDTO (
    @field:NotNull
    val exerciseId: Long,
    @field:NotNull
    val workoutId: Long,
    val rating: Int,
    val evaluation: String,
    val comments: String?,
    @field:ValidDate
    val completedAt: LocalDate?
)