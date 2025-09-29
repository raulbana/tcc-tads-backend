package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto

import java.sql.Time

data class UrinationDataDTO(
    val time: Time,
    val amount: String,
    val leakage: Boolean,
    val reason: String?,
    val urgency: Boolean?
)
