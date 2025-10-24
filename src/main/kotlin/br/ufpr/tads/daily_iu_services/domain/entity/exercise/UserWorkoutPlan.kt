package br.ufpr.tads.daily_iu_services.domain.entity.exercise

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "userWorkoutPlan")
data class UserWorkoutPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    val user: User,

    @OneToOne
    @JoinColumn(name = "workoutPlanId", referencedColumnName = "id")
    val plan: WorkoutPlan,

    var startDate: LocalDateTime,
    var endDate: LocalDateTime?,
    var totalProgress: Int,
    var weekProgress: Int,
    var currentWeek: Int,
    var nextWorkout: Int,
    var lastWorkoutDate: LocalDateTime?,
    var completed: Boolean,

    @Column(updatable = false, insertable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(updatable = false, insertable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
