package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto

import java.time.LocalTime

data class UrinationDataDTO(
    val time: LocalTime,
    val amount: String,
    val leakage: Boolean,
    val reason: String?,
    val urgency: Boolean?
)
