package br.ufpr.tads.daily_iu_services.adapter.output

import br.ufpr.tads.daily_iu_services.domain.entity.question.Question
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionsRepository: MongoRepository<Question, String> {
}