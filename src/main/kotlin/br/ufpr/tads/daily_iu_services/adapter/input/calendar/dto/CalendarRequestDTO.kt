package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto

import br.ufpr.tads.daily_iu_services.domain.validator.ValidDate

data class CalendarRequestDTO(
    @field:ValidDate
    val date: String,
    val leakageLevel: String,
    val notesPreview: String?,
    val urinationData: List<UrinationDataDTO>?
)