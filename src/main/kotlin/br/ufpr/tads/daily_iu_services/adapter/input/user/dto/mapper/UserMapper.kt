package br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.PatientProfileDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.PreferencesDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserSimpleDTO
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import br.ufpr.tads.daily_iu_services.domain.entity.user.Preferences
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper(uses = [UserMapperHelper::class])
interface UserMapper {

    companion object {
        val INSTANCE: UserMapper = Mappers.getMapper(UserMapper::class.java)
    }

    @Mapping(target = "profile", source = "profile", qualifiedByName = ["patientProfileToPatientProfileDTO"])
    @Mapping(target = "role", source = "role", qualifiedByName = ["roleToString"])
    @Mapping(target = "preferences", source = "preferences", qualifiedByName = ["preferencesToPreferencesDTO"])
    @Mapping(target = "profilePictureUrl", source = "profilePicture", qualifiedByName = ["mediaToUrl"])
    fun userToUserDTO(user: User): UserDTO

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "profilePictureUrl", source = "user.profilePicture", qualifiedByName = ["mediaToUrl"])
    @Mapping(target = "role", source = "user.role", qualifiedByName = ["roleToString"])
    @Mapping(target = "curtidas", source = "curtidas")
    @Mapping(target = "salvos", source = "salvos")
    @Mapping(target = "postagens", source = "postagens")
    fun userToUserSimpleDTO(user: User, curtidas: Long, salvos: Long, postagens: Long): UserSimpleDTO

    @Mapping(target = "createdAt", defaultExpression = "java(java.time.LocalDateTime.now())")
    fun mediaDTOToEntity(dto: MediaDTO?): Media?

    @Named("preferencesDTOToPreferences")
    fun preferencesDTOToPreferences(preferencesDTO: PreferencesDTO?): Preferences?

    @Named("preferencesToPreferencesDTO")
    fun preferencesToPreferencesDTO(preferences: Preferences): PreferencesDTO

    @Named("patientProfileToPatientProfileDTO")
    fun patientProfileToPatientProfileDTO(profile: PatientProfile?): PatientProfileDTO?
}