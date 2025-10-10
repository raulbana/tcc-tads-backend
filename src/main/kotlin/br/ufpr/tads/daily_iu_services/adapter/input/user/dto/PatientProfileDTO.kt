package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import java.time.LocalDate

data class PatientProfileDTO(
    val birthDate: LocalDate,
    val gender: String
)
