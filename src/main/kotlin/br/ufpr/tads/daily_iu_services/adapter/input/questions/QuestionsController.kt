package br.ufpr.tads.daily_iu_services.adapter.input.questions

import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.OnboardCompleteDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.OnboardSubmitDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.QuestionDTO
import br.ufpr.tads.daily_iu_services.domain.service.QuestionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/questions")
@Tag(name = "Questionário inicial", description = "Endpoints para gerenciar questionário de onboarding")
class QuestionsController(private val service: QuestionService) {

    @GetMapping("/onboard")
    @Operation(summary = "Obter perguntas iniciais", description = "Recupera as perguntas iniciais do onboarding")
    fun getInitialQuestions(@RequestHeader(value = "x-user-id", required = false) userId: Long?): ResponseEntity<List<QuestionDTO>> {
        return ResponseEntity.ok(service.getInitialQuestions(userId))
    }

    @PostMapping("/onboard")
    @Operation(summary = "Enviar respostas do onboarding", description = "Envia as respostas para as perguntas do questionário de onboarding")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Respostas enviadas com sucesso"),
        ApiResponse(responseCode = "409", description = "Usuário já possui um plano de treino ativo")
    )
    fun submitOnboardingAnswers(@RequestBody @Valid request: OnboardSubmitDTO): ResponseEntity<OnboardCompleteDTO> {
        return ResponseEntity.ok(service.submitOnboardingAnswers(request))
    }
}