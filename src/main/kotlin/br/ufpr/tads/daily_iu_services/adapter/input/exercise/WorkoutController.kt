package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutDTO
import br.ufpr.tads.daily_iu_services.domain.service.WorkoutService
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
@RequestMapping("/v1/exercise/workout")
@Tag(name = "Treino", description = "Endpoints para gerenciamento de treinos")
class WorkoutController(private val service: WorkoutService) {

    @PostMapping
    @Operation(summary = "Criar Treino", description = "Criar um novo treino")
    @ApiResponse(responseCode = "201", description = "Treino criado com sucesso")
    fun createWorkout(@RequestBody @Valid request: WorkoutCreatorDTO): ResponseEntity<WorkoutDTO> {
        return ResponseEntity.status(201).body(service.createWorkout(request))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Treino", description = "Atualizar um treino existente")
    fun updateWorkout(@PathVariable id: Long, @RequestBody @Valid request: WorkoutCreatorDTO): ResponseEntity<WorkoutDTO> {
        return ResponseEntity.ok(service.updateWorkout(id, request))
    }

    @GetMapping
    @Operation(summary = "Listar Treinos", description = "Listar todos os treinos disponíveis")
    fun listWorkouts(): ResponseEntity<List<WorkoutDTO>> {
        return ResponseEntity.ok(service.listWorkouts())
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Treino", description = "Deletar um treino existente")
    @ApiResponse(responseCode = "204", description = "Treino deletado com sucesso")
    fun deleteWorkout(@PathVariable id: Long): ResponseEntity<Void> {
        service.deleteWorkout(id)
        return ResponseEntity.noContent().build()
    }
}