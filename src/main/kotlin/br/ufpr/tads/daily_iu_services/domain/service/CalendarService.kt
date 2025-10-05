package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.UrinationDataDTO
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.mapper.CalendarMapper
import br.ufpr.tads.daily_iu_services.adapter.output.calendar.CalendarRepository
import br.ufpr.tads.daily_iu_services.adapter.output.calendar.UrinationDataRepository
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
    private val calendarRepository: CalendarRepository,
    private val urinationDataRepository: UrinationDataRepository
) {

    fun getCalendarEvents(userId: Long, from: LocalDate?, to: LocalDate?): HashMap<String, CalendarDayDTO> {
        val calendarEvents: List<CalendarDay> =
            if (from != null && to != null) {
                calendarRepository.findByDateRangeAndUserId(from, to, userId)
            } else {
                val currentDate = LocalDate.now()
                calendarRepository.findByMonthAndUserId(currentDate.monthValue, currentDate.year, userId)
            }

        return calendarEvents.map { CalendarMapper.INSTANCE.calendarDaytoDTO(it, urinationDataRepository.findBycalendarDay(it)) }
            .associateBy { it.date.toString() } as HashMap<String, CalendarDayDTO>
    }

    @Transactional
    fun createOrUpdateEvent(userId: Long, request: CalendarRequestDTO): CalendarDayDTO {
        var event: CalendarDay? = calendarRepository.findByDateAndUserId(request.date, userId)
        val data: List<UrinationData>?

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

            data = CalendarMapper.INSTANCE
                .urinationDataDTOListToEntity(request.urinationData)
        } else {
            data = processUrinationData(
                request.urinationData,
                urinationDataRepository.findBycalendarDay(event)
            )
        }

        event = calendarRepository.save(event)

        data?.forEach {
            it.calendarDay = event
            urinationDataRepository.save(it)
        }

        return CalendarMapper.INSTANCE.calendarDaytoDTO(event, data)
    }

    fun processUrinationData(
        newEntries: List<UrinationDataDTO>?,
        oldEntries: List<UrinationData>? = null
    ): List<UrinationData> {
        val oldList = oldEntries ?: emptyList()
        val newListEntities = CalendarMapper.INSTANCE.urinationDataDTOListToEntity(newEntries) ?: emptyList()

        // Entradas para adicionar: estão nos novos, mas não nos antigos
        val toAdd = newListEntities.filter { new -> oldList.none { old -> old == new } }

        // Entradas para remover: estão nos antigos, mas não nos novos
        val toRemove = oldList.filter { old -> newListEntities.none { new -> new == old } }
        toRemove.forEach { urinationDataRepository.delete(it) }

        // Entradas que permanecem: estão em ambos
        val toKeep = oldList.filter { old -> newListEntities.any { new -> new == old } }

        // Retorna lista final para persistência
        return toKeep + toAdd
    }
}