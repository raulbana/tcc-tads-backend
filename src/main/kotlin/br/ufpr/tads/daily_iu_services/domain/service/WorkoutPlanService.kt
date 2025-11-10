package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutPlanRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlanWorkout
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class WorkoutPlanService(
    private val workoutPlanRepository: WorkoutPlanRepository,
    private val workoutRepository: WorkoutRepository
) {

    fun listWorkoutPlans(): List<WorkoutPlanDTO> {
        val workoutPlans = workoutPlanRepository.findAll()
        return workoutPlans.map { ExerciseMapper.INSTANCE.workoutPlanEntityToDTO(it) }
    }

    fun getWorkoutPlanById(id: Long): WorkoutPlan? {
        val workoutPlan: WorkoutPlan? = workoutPlanRepository.findById(id).orElse(null)
        return workoutPlan
    }

    fun createWorkoutPlan(request: WorkoutPlanCreatorDTO): WorkoutPlanDTO {
        val newWorkoutPlan = WorkoutPlan(
            name = request.name,
            description = request.description,
            daysPerWeek = request.daysPerWeek,
            totalWeeks = request.totalWeeks,
            iciqScoreMin = request.iciqScoreMin,
            iciqScoreMax = request.iciqScoreMax,
            ageMin = request.ageMin,
            ageMax = request.ageMax
        )

        val workouts = request.workoutIds.map { (order, workoutId) ->
            val workout = workoutRepository.findById(workoutId).orElseThrow {
                NotFoundException("Treino com id $workoutId não encontrado")
            }
            WorkoutPlanWorkout(
                workoutOrder = order,
                workoutPlan = newWorkoutPlan,
                workout = workout
            )
        }

        newWorkoutPlan.workouts.addAll(workouts)
        val savedWorkoutPlan = workoutPlanRepository.save(newWorkoutPlan)
        return ExerciseMapper.INSTANCE.workoutPlanEntityToDTO(savedWorkoutPlan)
    }

    fun updateWorkoutPlan(id: Long, request: WorkoutPlanCreatorDTO): WorkoutPlanDTO {
        val existingWorkoutPlan = workoutPlanRepository.findById(id)
            .orElseThrow { NotFoundException("Plano de treino com id $id não encontrado") }

        existingWorkoutPlan.name = request.name
        existingWorkoutPlan.description = request.description
        existingWorkoutPlan.daysPerWeek = request.daysPerWeek
        existingWorkoutPlan.totalWeeks = request.totalWeeks
        existingWorkoutPlan.iciqScoreMin = request.iciqScoreMin
        existingWorkoutPlan.iciqScoreMax = request.iciqScoreMax
        existingWorkoutPlan.ageMin = request.ageMin
        existingWorkoutPlan.ageMax = request.ageMax

        existingWorkoutPlan.workouts.clear()
        val updatedWorkouts = request.workoutIds.map { (order, workoutId) ->
            val workout = workoutRepository.findById(workoutId).orElseThrow {
                NotFoundException("Treino com id $workoutId não encontrado")
            }
            WorkoutPlanWorkout(
                workoutOrder = order,
                workoutPlan = existingWorkoutPlan,
                workout = workout
            )
        }

        existingWorkoutPlan.workouts.addAll(updatedWorkouts)
        val savedWorkoutPlan = workoutPlanRepository.save(existingWorkoutPlan)
        return ExerciseMapper.INSTANCE.workoutPlanEntityToDTO(savedWorkoutPlan)
    }

    fun deleteWorkoutPlan(id: Long) {
        val existingWorkoutPlan = workoutPlanRepository.findById(id)
            .orElseThrow { NotFoundException("Plano de treino com id $id não encontrado") }
        workoutPlanRepository.delete(existingWorkoutPlan)
    }

    fun findSuitableWorkoutPlans(age: Int, iciqScore: Int): List<WorkoutPlan> =
        workoutPlanRepository.findByAgeMinLessThanEqualAndAgeMaxGreaterThanEqualAndIciqScoreMinLessThanEqualAndIciqScoreMaxGreaterThanEqual(
            age, age, iciqScore, iciqScore
        )
}