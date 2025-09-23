package br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.PatientProfileDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.PreferencesDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserDTO
import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import br.ufpr.tads.daily_iu_services.domain.entity.user.Preferences
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper
interface UserMapper {

    companion object {
        val INSTANCE: UserMapper = Mappers.getMapper(UserMapper::class.java)
    }

    @Mapping(target = "profile", source = "profile", qualifiedByName = ["patientProfileToPatientProfileDTO"])
    @Mapping(target = "preferences", source = "preferences", qualifiedByName = ["preferencesToPreferencesDTO"])
    fun userToUserDTO(user: User): UserDTO

    @Named("preferencesToPreferencesDTO")
    fun preferencesToPreferencesDTO(preferences: Preferences): PreferencesDTO

    @Named("patientProfileToPatientProfileDTO")
    fun patientProfileToPatientProfileDTO(profile: PatientProfile): PatientProfileDTO
}