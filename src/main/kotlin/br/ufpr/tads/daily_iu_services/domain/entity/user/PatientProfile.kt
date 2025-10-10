package br.ufpr.tads.daily_iu_services.domain.entity.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "patientProfile")
class PatientProfile (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val birthDate: LocalDate,
    val gender: String
    /*
    val q1Score: Double,
    val q2Score: Double,
    val q3Score: Double,
    val q4Score: Double*/
)