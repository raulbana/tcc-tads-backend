package br.ufpr.tads.daily_iu_services.adapter.output.exercise

import br.ufpr.tads.daily_iu_services.domain.entity.exercise.UserWorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserWorkoutPlanRepository: JpaRepository<UserWorkoutPlan, Long> {

    fun findByUserAndCompletedFalse(user: User): UserWorkoutPlan?

    fun findByUser(user: User): List<UserWorkoutPlan>
}