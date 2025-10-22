package br.ufpr.tads.daily_iu_services.domain.entity.exercise

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "workoutExercise")
data class WorkoutExercise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val exerciseOrder: Int,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "workoutId", referencedColumnName = "id")
    val workout: Workout,

    @ManyToOne
    @JoinColumn(name = "exerciseId", referencedColumnName = "id")
    val exercise: Exercise
)
