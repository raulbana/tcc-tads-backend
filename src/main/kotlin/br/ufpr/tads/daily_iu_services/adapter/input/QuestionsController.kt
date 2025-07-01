package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.adapter.input.dto.QuestionDTO
import br.ufpr.tads.daily_iu_services.domain.service.QuestionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/questions")
class QuestionsController(private val service: QuestionService) {

    @GetMapping("/onboard")
    fun getInitialQuestions(): ResponseEntity<List<QuestionDTO>>{
        return ResponseEntity.ok(service.getInitialQuestions())
    }
}