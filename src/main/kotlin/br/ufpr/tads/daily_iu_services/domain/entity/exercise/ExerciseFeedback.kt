package br.ufpr.tads.daily_iu_services.domain.entity.exercise

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "exerciseFeedback")
data class ExerciseFeedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "exerciseId")
    val exercise: Exercise,

    @OneToOne
    @JoinColumn(name = "workoutId")
    val workout: Workout,

    @OneToOne
    @JoinColumn(name = "userId")
    val user: User,

    var evaluation: String,
    var rating: Int,
    var comments: String,

    val completedAt: LocalDate = LocalDate.now()
)