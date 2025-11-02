package br.ufpr.tads.daily_iu_services.domain.entity.exercise

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "workoutPlan")
data class WorkoutPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,
    var description: String,
    var daysPerWeek: Int,
    var totalWeeks: Int,
    var iciqScoreMin: Int,
    var iciqScoreMax: Int,
    var ageMin: Int,
    var ageMax: Int,

    @OneToMany(mappedBy = "workoutPlan", cascade = [CascadeType.ALL], orphanRemoval = true)
    val workouts: MutableList<WorkoutPlanWorkout> = mutableListOf(),

    @Column(insertable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
