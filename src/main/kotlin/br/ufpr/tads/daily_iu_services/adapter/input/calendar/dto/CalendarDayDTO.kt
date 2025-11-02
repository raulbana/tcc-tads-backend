package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class CalendarDayDTO(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,
    val leakageLevel: String,
    val eventsCount: Int,
    val completedExercises: Int,
    val notesPreview: String?,
    val urinationData: List<UrinationDataDTO>?,
    val dayTitle: String,
    var dayNumber: Int,
    var isToday: Boolean
)