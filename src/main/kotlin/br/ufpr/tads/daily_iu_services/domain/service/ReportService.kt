package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportParamDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportUserDTO
import br.ufpr.tads.daily_iu_services.adapter.output.calendar.CalendarRepository
import br.ufpr.tads.daily_iu_services.adapter.output.calendar.UrinationDataRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.PatientProfileRepository
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.UrinationDataDTO
import br.ufpr.tads.daily_iu_services.domain.entity.calendar.UrinationData
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

@Service
class ReportService(
    private val userRepository: UserRepository,
    private val profileRepository: PatientProfileRepository,
    private val calendarRepository: CalendarRepository,
    private val urinationDataRepository: UrinationDataRepository
) {
    fun getReport(userId: Long, params: ReportParamDTO): ReportDTO {
        val user = userRepository.findById(userId).orElseThrow()
        val profile = profileRepository.findById(user.profile.id!!).orElseThrow()
        val days = calendarRepository.findByDateRangeAndUserId(
            params.startDate.toString(),
            params.endDate.toString(),
            userId
        )

        val userDTO = ReportUserDTO(
            user.name,
            Period.between(LocalDate.parse(profile.birthDate), LocalDate.now()).years,
            profile.gender,
        )

        val historyDTO = days.map { day ->
            val dayUrinations = urinationDataRepository.findBycalendarDay(day)
                ?.map { it.toDTO() }

            CalendarDayDTO(
                date = day.date,
                leakageLevel = day.leakageLevel.toString(),
                completedExercises = day.completedExercises,
                notesPreview = day.notesPreview,
                dayTitle = day.dayTitle,
                eventsCount = day.eventsCount,
                dayNumber = LocalDate.parse(day.date).dayOfMonth,
                isToday = LocalDate.parse(day.date).isEqual(LocalDate.now()),
                urinationData = dayUrinations
            )
        }

        return ReportDTO(
            user = userDTO,
            history = historyDTO,
            generatedAt = LocalDateTime.now()
        )
    }

    fun UrinationData.toDTO() = UrinationDataDTO(
        time = time,
        amount = amount.toString(),
        leakage = leakage,
        reason = reason,
        urgency = urgency
    )
}