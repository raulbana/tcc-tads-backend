package br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface AccessibilityMapper {

    companion object {
        val INSTANCE: AccessibilityMapper = Mappers.getMapper(AccessibilityMapper::class.java)
    }

    @Mapping(target = "isBigFont", source = "user.preferences.bigFont")
    @Mapping(target = "isHighContrast", source = "user.preferences.highContrast")
    fun UserToAccessibilityDTO(user: User): AccessibilityDTO
}