package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseAttributeDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCategoryDTO
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttribute
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeType
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseCategory
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper(imports = [ExerciseAttributeType::class])
interface ExerciseMapper {

    companion object{
        val INSTANCE: ExerciseMapper = Mappers.getMapper(ExerciseMapper::class.java)
    }

    @Mapping(target = "type", expression = "java(ExerciseAttributeType.Companion.fromLabel(attribute.getType()).getValue())")
    fun exerciseAttributeToDTO(attribute: ExerciseAttribute): ExerciseAttributeDTO

    fun exerciseCategoryToDTO(category: ExerciseCategory): ExerciseCategoryDTO
}