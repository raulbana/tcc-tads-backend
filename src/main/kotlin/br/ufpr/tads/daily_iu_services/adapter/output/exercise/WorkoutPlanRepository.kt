package br.ufpr.tads.daily_iu_services.adapter.output.exercise

import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkoutPlanRepository : JpaRepository<WorkoutPlan, Long> {
    fun findByAgeMinLessThanEqualAndAgeMaxGreaterThanEqualAndIciqScoreMinLessThanEqualAndIciqScoreMaxGreaterThanEqual(
        ageLower: Int,
        ageUpper: Int,
        iciqLower: Int,
        iciqUpper: Int
    ): List<WorkoutPlan>
}