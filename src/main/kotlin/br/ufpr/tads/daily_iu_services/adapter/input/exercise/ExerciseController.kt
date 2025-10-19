package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseFeedbackCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.UserWorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/exercise")
@Tag(name = "Exercício", description = "Endpoints para gerenciamento de exercícios")
class ExerciseController {

    @PostMapping
    @Operation(summary = "Criar Exercício", description = "Criar um novo exercício")
    @ApiResponse(responseCode = "201", description = "Exercício criado com sucesso")
    fun createExercise(@RequestBody @Valid request: ExerciseCreatorDTO): ResponseEntity<ExerciseDTO> {
        TODO("Not implemented yet")
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Exercício", description = "Atualizar um exercício existente")
    fun updateExercise(@PathVariable id: Long, @RequestBody @Valid request: ExerciseCreatorDTO): ResponseEntity<ExerciseDTO> {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout")
    @Operation(summary = "Criar Treino", description = "Criar um novo treino")
    @ApiResponse(responseCode = "201", description = "Treino criado com sucesso")
    fun createWorkout(@RequestBody @Valid request: WorkoutCreatorDTO): ResponseEntity<WorkoutDTO> {
        TODO("Not implemented yet")
    }

    @PutMapping("/workout/{id}")
    @Operation(summary = "Atualizar Treino", description = "Atualizar um treino existente")
    fun updateWorkout(@PathVariable id: Long, @RequestBody @Valid request: WorkoutCreatorDTO): ResponseEntity<WorkoutDTO> {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout/plan")
    @Operation(summary = "Criar Plano de Treino", description = "Criar um novo plano de treino")
    @ApiResponse(responseCode = "201", description = "Plano de treino criado com sucesso")
    fun createWorkoutPlan(@RequestBody @Valid request: WorkoutPlanCreatorDTO): ResponseEntity<WorkoutPlanDTO> {
        TODO("Not implemented yet")
    }

    @PutMapping("/workout/plan/{id}")
    @Operation(summary = "Atualizar Plano de Treino", description = "Atualizar um plano de treino existente")
    fun updateWorkoutPlan(@PathVariable id: Long, @RequestBody @Valid request: WorkoutPlanCreatorDTO): ResponseEntity<WorkoutPlanDTO> {
        TODO("Not implemented yet")
    }

    @GetMapping("/workout/plan")
    @Operation(summary = "Obter Plano de Treino", description = "Recuperar o plano de treino de um usuário")
    fun getWorkoutPlan(@RequestHeader("x-user-id") userId: Long): ResponseEntity<UserWorkoutPlanDTO> {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout/completion")
    @Operation(summary = "Registrar Conclusão de Treino", description = "Registrar a conclusão de um treino")
    @ApiResponse(responseCode = "204", description = "Conclusão de treino registrada com sucesso")
    fun logWorkoutCompletion(@RequestHeader("x-user-id") userId: Long): ResponseEntity<Void> {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout/feedback")
    @Operation(summary = "Registra Feedback de Treino", description = "Cria uma nova entrada de feedback de treino")
    @ApiResponse(responseCode = "204", description = "Feedback de treino registrado com sucesso")
    fun createWorkoutFeedback(@RequestHeader("x-user-id") userId: Long, @RequestBody request: List<ExerciseFeedbackCreatorDTO>) {
        TODO("Not implemented yet")
    }
}