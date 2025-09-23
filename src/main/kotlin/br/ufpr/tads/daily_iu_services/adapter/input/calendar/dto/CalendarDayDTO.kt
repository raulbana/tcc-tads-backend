package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto

data class CalendarDayDTO(
    val date: String,
    val leakageLevel: String,
    val eventsCount: Int,
    val completedExercises: Int,
    val notesPreview: String?,
    val urinationData: List<UrinationDataDTO>?,
    val dayTitle: String,
    var dayNumber: Int,
    var isToday: Boolean
)