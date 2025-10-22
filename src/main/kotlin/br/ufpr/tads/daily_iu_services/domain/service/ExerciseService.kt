package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseAttributeRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseCategoryRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseRepository
import br.ufpr.tads.daily_iu_services.adapter.output.media.MediaRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Exercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeExercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseMedia
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseCategoryRepository: ExerciseCategoryRepository,
    private val exerciseAttributeRepository: ExerciseAttributeRepository,
    private val mediaRepository: MediaRepository
) {

    fun listExercises(): List<ExerciseDTO> {
        val exercises = exerciseRepository.findAll()
        return exercises.map { ExerciseMapper.INSTANCE.exerciseEntityToDTO(it) }
    }

    fun createExercise(request: ExerciseCreatorDTO): ExerciseDTO {
        val category = exerciseCategoryRepository.findById(request.categoryId)
            .orElseThrow { NotFoundException("Categoria de exercício com id ${request.categoryId} não encontrada") }

        val exercise = Exercise(
            title = request.title,
            instructions = request.instructions,
            category = category,
            repetitions = request.repetitions,
            sets = request.sets,
            restTime = request.restTime,
            duration = request.duration
        )

        val medias = request.media
            .map { ExerciseMapper.INSTANCE.mediaDTOToEntity(it) }
            .map { if (it.id == null) mediaRepository.save(it) else it }
            .map { ExerciseMedia(exercise = exercise, media = it) }

        val exerciseAttributes = exerciseAttributeRepository.findAllById(request.attributes)
            .map { ExerciseAttributeExercise(exercise = exercise, attribute = it) }

        exercise.media.addAll(medias)
        exercise.attributes.addAll(exerciseAttributes)

        val savedExercise = exerciseRepository.save(exercise)
        return ExerciseMapper.INSTANCE.exerciseEntityToDTO(savedExercise)
    }

    fun updateExercise(id: Long, request: ExerciseCreatorDTO): ExerciseDTO {
        val existingExercise = exerciseRepository.findById(id)
            .orElseThrow { NotFoundException("Exercício com id $id não encontrado") }

        val newCategory = exerciseCategoryRepository.findById(request.categoryId)
            .orElseThrow { NotFoundException("Categoria de exercício com id ${request.categoryId} não encontrada") }

        existingExercise.title = request.title
        existingExercise.instructions = request.instructions
        existingExercise.category = newCategory
        existingExercise.repetitions = request.repetitions
        existingExercise.sets = request.sets
        existingExercise.restTime = request.restTime
        existingExercise.duration = request.duration
        existingExercise.media.clear()
        existingExercise.attributes.clear()

        val updatedMedias = request.media
            .map { ExerciseMapper.INSTANCE.mediaDTOToEntity(it) }
            .map { if (it.id == null) mediaRepository.save(it) else it }
            .map { ExerciseMedia(exercise = existingExercise, media = it) }

        val updatedAttributes = exerciseAttributeRepository.findAllById(request.attributes)
            .map { ExerciseAttributeExercise(exercise = existingExercise, attribute = it) }

        existingExercise.media.addAll(updatedMedias)
        existingExercise.attributes.addAll(updatedAttributes)

        val savedExercise = exerciseRepository.save(existingExercise)
        return ExerciseMapper.INSTANCE.exerciseEntityToDTO(savedExercise)
    }

    fun deleteExercise(id: Long) {
        val exercise = exerciseRepository.findById(id)
            .orElseThrow { NotFoundException("Exercício com id $id não encontrado") }
        exerciseRepository.delete(exercise)
    }
}