package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class PatientProfileDTO(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthDate: LocalDate,
    val gender: String,
    val iciq3answer: Int,
    val iciq4answer: Int,
    val iciq5answer: Int,
    val iciqScore: Int,
    val urinationLoss: String
)