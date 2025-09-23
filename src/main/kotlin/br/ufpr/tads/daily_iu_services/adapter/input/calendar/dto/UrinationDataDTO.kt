package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto

data class UrinationDataDTO(
    val time: String,
    val amount: String,
    val leakage: Boolean,
    val reason: String?,
    val urgency: Boolean?
)
