package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportParamDTO
import br.ufpr.tads.daily_iu_services.adapter.output.calendar.CalendarRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.mapper.CalendarMapper
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.mapper.ReportMapper
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.mapper.ReportUserMapper
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val userRepository: UserRepository,
    private val calendarRepository: CalendarRepository,
) {
    fun getReport(userId: Long, params: ReportParamDTO): ReportDTO {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Usuário não encontrado com id $userId") }
        val profile = user.profile ?: throw IllegalArgumentException("Usuário com id $userId não possui perfil de paciente associado")

        val days = calendarRepository.findByDateRangeAndUserId(
            params.from,
            params.to,
            userId
        )

        val userDTO = ReportUserMapper.userToReportUserDTO(user, profile)

        val historyDTO = days.map { day ->
            CalendarMapper.INSTANCE.calendarDaytoDTO(day)
        }

        return ReportMapper.reportToReportDTO(userDTO, historyDTO)
    }
}