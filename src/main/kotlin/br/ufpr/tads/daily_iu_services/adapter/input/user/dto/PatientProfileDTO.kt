package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import br.ufpr.tads.daily_iu_services.domain.validator.ValidDate
import com.fasterxml.jackson.annotation.JsonFormat
import org.jetbrains.annotations.NotNull
import java.time.LocalDate

data class PatientProfileDTO(
    @field:NotNull
    @field:ValidDate
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthDate: LocalDate,

    val gender: String,

    @field:NotNull
    val iciq3answer: Int,

    @field:NotNull
    val iciq4answer: Int,

    @field:NotNull
    val iciq5answer: Int,

    @field:NotNull
    val iciqScore: Int,

    val urinationLoss: String
)