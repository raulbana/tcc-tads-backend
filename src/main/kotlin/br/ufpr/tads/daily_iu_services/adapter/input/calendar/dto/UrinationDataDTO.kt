package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalTime

data class UrinationDataDTO(
    val id: Long?,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time: LocalTime,
    val amount: String,
    val leakage: Boolean,
    val reason: String?,
    val urgency: Boolean?
)
