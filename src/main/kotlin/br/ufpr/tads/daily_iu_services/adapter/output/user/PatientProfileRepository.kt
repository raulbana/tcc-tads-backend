package br.ufpr.tads.daily_iu_services.adapter.output.user

import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PatientProfileRepository: JpaRepository<PatientProfile, Long> {
}