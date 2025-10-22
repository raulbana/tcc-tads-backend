package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseDTO
import br.ufpr.tads.daily_iu_services.domain.service.ExerciseService
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
@RequestMapping("/v1/exercise")
@Tag(name = "Exercício", description = "Endpoints para gerenciamento de exercícios")
class ExerciseController(private val service: ExerciseService) {

    @PostMapping
    @Operation(summary = "Criar Exercício", description = "Criar um novo exercício")
    @ApiResponse(responseCode = "201", description = "Exercício criado com sucesso")
    fun createExercise(@RequestBody @Valid request: ExerciseCreatorDTO): ResponseEntity<ExerciseDTO> {
        return ResponseEntity.status(201).body(service.createExercise(request))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Exercício", description = "Atualizar um exercício existente")
    fun updateExercise(@PathVariable id: Long, @RequestBody @Valid request: ExerciseCreatorDTO): ResponseEntity<ExerciseDTO> {
        return ResponseEntity.ok(service.updateExercise(id, request))
    }

    @GetMapping
    @Operation(summary = "Listar Exercícios", description = "Listar todos os exercícios disponíveis")
    fun listExercises(): ResponseEntity<List<ExerciseDTO>> {
        return ResponseEntity.ok(service.listExercises())
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Exercício", description = "Deletar um exercício existente")
    @ApiResponse(responseCode = "204", description = "Exercício deletado com sucesso")
    fun deleteExercise(@PathVariable id: Long): ResponseEntity<Void> {
        service.deleteExercise(id)
        return ResponseEntity.noContent().build()
    }
}