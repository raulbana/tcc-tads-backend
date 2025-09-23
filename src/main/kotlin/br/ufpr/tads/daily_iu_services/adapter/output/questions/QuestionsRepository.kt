package br.ufpr.tads.daily_iu_services.adapter.output.questions

import br.ufpr.tads.daily_iu_services.domain.entity.question.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionsRepository : JpaRepository<Question, Long>