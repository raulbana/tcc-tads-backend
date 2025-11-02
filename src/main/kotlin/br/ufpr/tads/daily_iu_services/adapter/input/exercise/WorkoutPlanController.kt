package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.domain.service.WorkoutPlanService
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
@RequestMapping("/v1/exercise/workout/plan")
@Tag(name = "Plano de Treino", description = "Endpoints para gerenciamento de planos de treino")
class WorkoutPlanController(private val workoutPlanService: WorkoutPlanService) {

    @PostMapping
    @Operation(summary = "Criar Plano de Treino", description = "Criar um novo plano de treino")
    @ApiResponse(responseCode = "201", description = "Plano de treino criado com sucesso")
    fun createWorkoutPlan(@RequestBody @Valid request: WorkoutPlanCreatorDTO): ResponseEntity<WorkoutPlanDTO> {
        return ResponseEntity.status(201).body(workoutPlanService.createWorkoutPlan(request))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Plano de Treino", description = "Atualizar um plano de treino existente")
    fun updateWorkoutPlan(@PathVariable id: Long, @RequestBody @Valid request: WorkoutPlanCreatorDTO): ResponseEntity<WorkoutPlanDTO> {
        return ResponseEntity.ok(workoutPlanService.updateWorkoutPlan(id, request))
    }

    @GetMapping
    @Operation(summary = "Listar Planos de Treino", description = "Listar todos os planos de treino disponíveis")
    fun listWorkoutPlans(): ResponseEntity<List<WorkoutPlanDTO>> {
        return ResponseEntity.ok(workoutPlanService.listWorkoutPlans())
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Plano de Treino", description = "Deletar um plano de treino existente")
    @ApiResponse(responseCode = "204", description = "Plano de treino deletado com sucesso")
    fun deleteWorkoutPlan(@PathVariable id: Long): ResponseEntity<Void> {
        workoutPlanService.deleteWorkoutPlan(id)
        return ResponseEntity.noContent().build()
    }
}