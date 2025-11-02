package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCategoryDTO
import br.ufpr.tads.daily_iu_services.domain.service.ExerciseCategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/exercise/category")
@Tag(name = "Categoria de Exercício", description = "Endpoints para gerenciamento de categorias de exercício")
class ExerciseCategoryController(private val service: ExerciseCategoryService) {

    @PostMapping
    @Operation(summary = "Criar Categoria", description = "Cria uma nova categoria de exercício")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    fun createCategory(@RequestBody @Valid request: ExerciseCategoryDTO): ResponseEntity<ExerciseCategoryDTO> {
        return ResponseEntity.status(201).body(service.createCategory(request))
    }

    @GetMapping
    @Operation(summary = "Buscar Todas Categorias", description = "Recupera todas as categorias de exercício")
    fun getAllCategories(): ResponseEntity<List<ExerciseCategoryDTO>> {
        return ResponseEntity.ok(service.getAllCategories())
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Categoria", description = "Atualiza uma categoria de exercício existente pelo ID")
    fun updateCategory(@PathVariable id: Long, @RequestBody @Valid request: ExerciseCategoryDTO): ResponseEntity<ExerciseCategoryDTO> {
        return ResponseEntity.ok(service.updateCategory(id, request))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Categoria", description = "Exclui uma categoria de exercício pelo ID")
    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        service.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }
}