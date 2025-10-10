package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CategoryDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper.ContentMapper
import br.ufpr.tads.daily_iu_services.adapter.output.content.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun createCategory(request: CategoryDTO): CategoryDTO {
        val category = ContentMapper.INSTANCE.categoryDTOToEntity(request)
        return ContentMapper.INSTANCE.categoryToDTO(categoryRepository.save(category))
    }

    fun getAllCategories(): List<CategoryDTO> {
        return categoryRepository.findAll().map { ContentMapper.INSTANCE.categoryToDTO(it) }
    }

    fun updateCategory(id: Long, request: CategoryDTO): CategoryDTO {
        val category = categoryRepository.findById(id).orElseThrow { Exception("Category not found") }
        val updatedCategory = category.copy(name = request.name, description = request.description, auditable = request.auditable)
        return ContentMapper.INSTANCE.categoryToDTO(categoryRepository.save(updatedCategory))
    }

    fun deleteCategory(id: Long) {
        val category = categoryRepository.findById(id).orElseThrow { Exception("Category not found") }
        categoryRepository.delete(category)
    }
}