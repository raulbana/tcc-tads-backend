package br.ufpr.tads.daily_iu_services.adapter.input.content

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCategoryDTO
import br.ufpr.tads.daily_iu_services.domain.service.ContentCategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
@RequestMapping("/v1/content/category")
@Tag(name = "Categoria de Conteúdo", description = "Endpoints para gerenciamento de categorias de conteúdo")
class ContentCategoryController(private val categoryService: ContentCategoryService) {

    @PostMapping
    @Operation(summary = "Criar Categoria", description = "Cria uma nova categoria de conteúdo")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    fun createCategory(@RequestBody @Valid request: ContentCategoryDTO): ResponseEntity<ContentCategoryDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request))
    }

    @GetMapping
    @Operation(summary = "Buscar Todas Categorias", description = "Recupera todas as categorias de conteúdo")
    fun getAllCategories(): ResponseEntity<List<ContentCategoryDTO>> {
        return ResponseEntity.ok(categoryService.getAllCategories())
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Categoria", description = "Atualiza uma categoria de conteúdo existente pelo ID")
    fun updateCategory(@PathVariable id: Long, @RequestBody @Valid request: ContentCategoryDTO): ResponseEntity<ContentCategoryDTO> {
        return ResponseEntity.ok(categoryService.updateCategory(id, request))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Categoria", description = "Exclui uma categoria de conteúdo pelo ID")
    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }
}