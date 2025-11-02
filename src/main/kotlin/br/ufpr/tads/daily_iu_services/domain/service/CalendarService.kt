package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.mapper.CalendarMapper
import br.ufpr.tads.daily_iu_services.adapter.output.calendar.CalendarRepository
import br.ufpr.tads.daily_iu_services.domain.entity.calendar.CalendarDay
import br.ufpr.tads.daily_iu_services.domain.entity.calendar.LeakageLevel
import br.ufpr.tads.daily_iu_services.domain.entity.calendar.UrinationData
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Service
class CalendarService(
    private val calendarRepository: CalendarRepository
) {

    fun getCalendarEvents(userId: Long, from: LocalDate?, to: LocalDate?): HashMap<String, CalendarDayDTO> {
        val calendarEvents: List<CalendarDay> =
            if (from != null && to != null) {
                calendarRepository.findByDateRangeAndUserId(from, to, userId)
            } else {
                val currentDate = LocalDate.now()
                calendarRepository.findByMonthAndUserId(currentDate.monthValue, currentDate.year, userId)
            }

        return calendarEvents.map { CalendarMapper.INSTANCE.calendarDaytoDTO(it) }
            .associateBy { it.date.toString() } as HashMap<String, CalendarDayDTO>
    }

    @Transactional
    fun createOrUpdateEvent(userId: Long, request: CalendarRequestDTO): CalendarDayDTO {
        var event: CalendarDay? = calendarRepository.findByDateAndUserId(request.date, userId)

        if (event == null) {
            val level = LeakageLevel.from(request.leakageLevel)
            val date = request.date
            val dayTitle = date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale("pt", "BR")).subSequence(0, 3).toString()

            event = CalendarDay(
                date = request.date,
                userId = userId,
                leakageLevel = level,
                eventsCount = request.urinationData?.size ?: 0,
                completedExercises = 0,
                notesPreview = request.notesPreview,
                dayTitle = dayTitle
            )
        } else {
            event.leakageLevel = LeakageLevel.from(request.leakageLevel)
            event.eventsCount = request.urinationData?.size ?: 0
            event.notesPreview = request.notesPreview
        }

        val data: List<UrinationData> = request.urinationData?.map {
            val urinationData = CalendarMapper.INSTANCE.urinationDataDTOtoEntity(it)
            urinationData.calendarDay = event
            urinationData
        } ?: emptyList()


        event.urinationData.clear()
        event.urinationData.addAll(data)

        return CalendarMapper.INSTANCE.calendarDaytoDTO(calendarRepository.save(event))
    }

    fun registerExerciseCompletion(userId: Long, date: LocalDate, exerciseCount: Int) {
        var event: CalendarDay? = calendarRepository.findByDateAndUserId(date, userId)

        if (event == null) {
            val level = LeakageLevel.NONE
            val dayTitle = date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale("pt", "BR")).subSequence(0, 3).toString()

            event = CalendarDay(
                date = date,
                userId = userId,
                leakageLevel = level,
                eventsCount = 0,
                completedExercises = exerciseCount,
                notesPreview = "",
                dayTitle = dayTitle
            )
        } else {
            event.completedExercises += exerciseCount
        }

        calendarRepository.save(event)
    }
}