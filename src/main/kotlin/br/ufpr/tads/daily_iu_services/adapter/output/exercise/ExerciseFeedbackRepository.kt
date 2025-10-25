package br.ufpr.tads.daily_iu_services.adapter.output.exercise

import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseFeedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseFeedbackRepository : JpaRepository<ExerciseFeedback, Long> {}