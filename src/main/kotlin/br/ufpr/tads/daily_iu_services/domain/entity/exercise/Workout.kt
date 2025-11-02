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
@Table(name = "workout")
data class Workout (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,
    var description: String,
    var totalDuration: Int,
    var difficultyLevel: String,

    @OneToMany(mappedBy = "workout", cascade = [CascadeType.ALL], orphanRemoval = true)
    val exercises: MutableList<WorkoutExercise> = mutableListOf(),

    @Column(insertable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)
