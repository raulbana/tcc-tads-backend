package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseAttributeDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseAttributeRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttribute
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeType
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class ExerciseAttributeService(private val repository: ExerciseAttributeRepository) {

    fun createAttribute(request: ExerciseAttributeDTO): ExerciseAttributeDTO {
        val attribute = ExerciseAttribute(
            name = request.name,
            description = request.description,
            type = ExerciseAttributeType.fromValue(request.type).label
        )

        return ExerciseMapper.INSTANCE.exerciseAttributeToDTO(repository.save(attribute))
    }

    fun getAllAttributes(): List<ExerciseAttributeDTO> {
        return repository.findAll().map { ExerciseMapper.INSTANCE.exerciseAttributeToDTO(it) }
    }

    fun updateAttribute(id: Long, request: ExerciseAttributeDTO): ExerciseAttributeDTO {
        val attribute = repository.findById(id).orElseThrow {
            NotFoundException("Atributo de exercício com id $id não encontrado")
        }
        val updatedAttribute = attribute.copy(
            name = request.name,
            description = request.description,
            type = ExerciseAttributeType.fromValue(request.type).label
        )
        return ExerciseMapper.INSTANCE.exerciseAttributeToDTO(repository.save(updatedAttribute))
    }

    fun deleteAttribute(id: Long) {
        val attribute = repository.findById(id).orElseThrow {
            NotFoundException("Atributo de exercício com id $id não encontrado")
        }
        repository.delete(attribute)
    }
}