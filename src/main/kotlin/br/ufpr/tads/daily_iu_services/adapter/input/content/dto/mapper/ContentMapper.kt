package br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CategoryDTO
import br.ufpr.tads.daily_iu_services.domain.entity.content.Category
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface ContentMapper {

    companion object {
        val INSTANCE: ContentMapper = Mappers.getMapper(ContentMapper::class.java)
    }

    fun categoryToDTO(entity: Category): CategoryDTO

    fun categoryDTOToEntity(dto: CategoryDTO): Category
}