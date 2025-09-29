package br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportUserDTO
import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import java.time.LocalDate
import java.time.Period

object ReportUserMapper {
    fun userToReportUserDTO(user: User, profile: PatientProfile): ReportUserDTO =
        ReportUserDTO(
            name = user.name,
            age = Period.between(profile.birthDate, LocalDate.now()).years,
            gender = profile.gender
        )
}