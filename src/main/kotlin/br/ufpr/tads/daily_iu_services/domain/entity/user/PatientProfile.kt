package br.ufpr.tads.daily_iu_services.domain.entity.user

import jakarta.persistence.Column
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
    val gender: String,

    var iciq3answer: Int,
    var iciq4answer: Int,
    var iciq5answer: Int,
    var iciqScore: Int,
    var urinationLoss: String,

    @Column(insertable = false, updatable = false)
    val createdAt: LocalDate = LocalDate.now(),
    @Column(insertable = false, updatable = false)
    val updatedAt: LocalDate = LocalDate.now()
)