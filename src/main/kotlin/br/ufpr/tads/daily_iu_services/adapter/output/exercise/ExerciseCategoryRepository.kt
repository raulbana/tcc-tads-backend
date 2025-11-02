package br.ufpr.tads.daily_iu_services.adapter.output.exercise

import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseCategoryRepository : JpaRepository<ExerciseCategory, Long> {
}