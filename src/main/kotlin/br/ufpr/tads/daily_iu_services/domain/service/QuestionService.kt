package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.dto.QuestionDTO
import br.ufpr.tads.daily_iu_services.adapter.input.dto.mapper.QuestionMapper
import br.ufpr.tads.daily_iu_services.adapter.output.QuestionsRepository
import org.springframework.stereotype.Service

@Service
class QuestionService(private val repository: QuestionsRepository) {

    fun getInitialQuestions(): List<QuestionDTO>{
        return repository.findAll().map { QuestionMapper.INSTANCE.questionToQuestionDTO(it) }
    }
}