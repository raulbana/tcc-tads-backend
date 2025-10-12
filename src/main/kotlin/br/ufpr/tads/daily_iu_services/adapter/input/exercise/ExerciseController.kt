package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/exercise")
@Tag(name = "Exercício", description = "Endpoints para gerenciamento de exercícios")
class ExerciseController {

    @PostMapping
    @Operation(summary = "Criar Exercício", description = "Criar um novo exercício")
    fun createExercise() {
        TODO("Not implemented yet")
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Exercício", description = "Atualizar um exercício existente")
    fun updateExercise(@PathVariable id: Long) {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout")
    @Operation(summary = "Criar Treino", description = "Criar um novo treino")
    fun createWorkout() {
        TODO("Not implemented yet")
    }

    @PutMapping("/workout/{id}")
    @Operation(summary = "Atualizar Treino", description = "Atualizar um treino existente")
    fun updateWorkout(@PathVariable id: Long) {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout/plan")
    @Operation(summary = "Criar Plano de Treino", description = "Criar um novo plano de treino")
    fun createWorkoutPlan() {
        TODO("Not implemented yet")
    }

    @PutMapping("/workout/plan/{id}")
    @Operation(summary = "Atualizar Plano de Treino", description = "Atualizar um plano de treino existente")
    fun updateWorkoutPlan(@PathVariable id: Long) {
        TODO("Not implemented yet")
    }

    @GetMapping("/workout/plan")
    @Operation(summary = "Obter Plano de Treino", description = "Recuperar o plano de treino de um usuário")
    fun getWorkoutPlans(@RequestHeader("x-user-id") userId: Long) {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout/completion")
    @Operation(summary = "Registrar Conclusão de Treino", description = "Registrar a conclusão de um treino")
    fun logWorkoutCompletion(@RequestHeader("x-user-id") userId: Long) {
        TODO("Not implemented yet")
    }

    @PostMapping("/workout/feedback")
    @Operation(summary = "Criar Feedback de Treino", description = "Criar uma nova entrada de feedback de treino")
    fun createWorkoutFeedback(@RequestHeader("x-user-id") userId: Long) {
        TODO("Not implemented yet")
    }
}