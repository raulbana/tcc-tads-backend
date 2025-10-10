package br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.QuestionDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.QuestionOptionDTO
import br.ufpr.tads.daily_iu_services.domain.entity.question.Question
import br.ufpr.tads.daily_iu_services.domain.entity.question.QuestionOption
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface QuestionMapper {

    companion object{
        val INSTANCE: QuestionMapper = Mappers.getMapper(QuestionMapper::class.java)
    }

    @Mapping(target = "id", source = "externalId")
    @Mapping(target = "type", expression = "java(question.getType().getValue())")
    fun questionToQuestionDTO(question: Question): QuestionDTO

}