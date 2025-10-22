package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Workout
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutExercise
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class WorkoutService(
    private val repository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
) {

    fun listWorkouts(): List<WorkoutDTO> {
        val workouts = repository.findAll()
        return workouts.map { ExerciseMapper.INSTANCE.workoutEntityToDTO(it) }
    }

    fun createWorkout(request: WorkoutCreatorDTO): WorkoutDTO {
        val newWorkout = Workout(
            name = request.name,
            description = request.description,
            totalDuration = request.totalDuration,
            difficultyLevel = request.difficultyLevel
        )

        val exercises = request.exerciseIds.map { (order, exerciseId) ->
            val exercise = exerciseRepository.findById(exerciseId).orElseThrow {
                NotFoundException("Exercício com id $exerciseId não encontrado")
            }
            WorkoutExercise(
                exerciseOrder = order,
                workout = newWorkout,
                exercise = exercise
            )
        }

        newWorkout.exercises.addAll(exercises)

        val savedWorkout = repository.save(newWorkout)
        return ExerciseMapper.INSTANCE.workoutEntityToDTO(savedWorkout)
    }

    fun updateWorkout(id: Long, request: WorkoutCreatorDTO): WorkoutDTO {
        val existingWorkout = repository.findById(id)
            .orElseThrow { NotFoundException("Treino com id $id não encontrado") }

        existingWorkout.name = request.name
        existingWorkout.description = request.description
        existingWorkout.totalDuration = request.totalDuration
        existingWorkout.difficultyLevel = request.difficultyLevel

        existingWorkout.exercises.clear()
        val updatedExercises = request.exerciseIds.map { (order, exerciseId) ->
            val exercise = exerciseRepository.findById(exerciseId).orElseThrow {
                NotFoundException("Exercício com id $exerciseId não encontrado")
            }
            WorkoutExercise(
                exerciseOrder = order,
                workout = existingWorkout,
                exercise = exercise
            )
        }
        existingWorkout.exercises.addAll(updatedExercises)

        val savedWorkout = repository.save(existingWorkout)
        return ExerciseMapper.INSTANCE.workoutEntityToDTO(savedWorkout)
    }

    fun deleteWorkout(id: Long) {
        val existingWorkout = repository.findById(id)
            .orElseThrow { NotFoundException("Treino com id $id não encontrado") }
        repository.delete(existingWorkout)
    }

}