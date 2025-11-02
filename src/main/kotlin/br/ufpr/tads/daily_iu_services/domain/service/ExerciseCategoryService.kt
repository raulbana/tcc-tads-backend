package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCategoryDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseCategoryRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseCategory
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class ExerciseCategoryService(private val repository: ExerciseCategoryRepository) {

    fun createCategory(request: ExerciseCategoryDTO): ExerciseCategoryDTO {
        val newCategory = ExerciseCategory(
            name = request.name,
            description = request.description
        )

        val savedCategory = repository.save(newCategory)
        return ExerciseMapper.INSTANCE.exerciseCategoryToDTO(savedCategory)
    }

    fun getAllCategories(): List<ExerciseCategoryDTO> {
        val categories = repository.findAll()
        return categories.map { ExerciseMapper.INSTANCE.exerciseCategoryToDTO(it) }
    }

    fun updateCategory(id: Long, request: ExerciseCategoryDTO): ExerciseCategoryDTO {
        val existingCategory = repository.findById(id).orElseThrow {
            NotFoundException("Categoria de exercício com ID $id não encontrada.")
        }

        val updatedCategory = existingCategory.copy(
            name = request.name,
            description = request.description
        )

        val savedCategory = repository.save(updatedCategory)
        return ExerciseMapper.INSTANCE.exerciseCategoryToDTO(savedCategory)
    }

    fun deleteCategory(id: Long) {
        val existingCategory = repository.findById(id).orElseThrow {
            NotFoundException("Categoria de exercício com ID $id não encontrada.")
        }

        repository.delete(existingCategory)
    }
}