package br.ufpr.tads.daily_iu_services.adapter.input.questions

import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.QuestionDTO
import br.ufpr.tads.daily_iu_services.domain.service.QuestionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/questions")
@Tag(name = "Questions", description = "Endpoints for managing onboarding questions")
class QuestionsController(private val service: QuestionService) {

    @GetMapping("/onboard")
    @Operation(summary = "Get Initial Questions", description = "Retrieve the initial onboarding questions")
    fun getInitialQuestions(): ResponseEntity<List<QuestionDTO>>{
        return ResponseEntity.ok(service.getInitialQuestions())
    }
}