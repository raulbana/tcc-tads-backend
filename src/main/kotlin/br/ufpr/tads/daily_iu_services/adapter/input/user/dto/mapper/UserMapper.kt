package br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.PatientProfileDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.PreferencesDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserSimpleDTO
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import br.ufpr.tads.daily_iu_services.domain.entity.user.Preferences
import br.ufpr.tads.daily_iu_services.domain.entity.user.Role
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import br.ufpr.tads.daily_iu_services.domain.entity.user.UserPermissionEnum
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper
interface UserMapper {

    companion object {
        val INSTANCE: UserMapper = Mappers.getMapper(UserMapper::class.java)
    }

    @Named("mediaToUrl")
    fun mediaToUrl(media: Media?) = media?.url

    @Named("roleToString")
    fun roleToString(role: Role) = UserPermissionEnum.fromLevel(role.permissionLevel).label

    @Mapping(target = "profile", source = "profile", qualifiedByName = ["patientProfileToPatientProfileDTO"])
    @Mapping(target = "role", source = "role", qualifiedByName = ["roleToString"])
    @Mapping(target = "preferences", source = "preferences", qualifiedByName = ["preferencesToPreferencesDTO"])
    @Mapping(target = "profilePictureUrl", source = "profilePicture", qualifiedByName = ["mediaToUrl"])
    fun userToUserDTO(user: User): UserDTO

    @Mapping(target = "profilePictureUrl", source = "profilePicture", qualifiedByName = ["mediaToUrl"])
    @Mapping(target = "role", source = "role", qualifiedByName = ["roleToString"])
    fun userToUserSimpleDTO(user: User): UserSimpleDTO

    @Mapping(target = "createdAt", defaultExpression = "java(java.time.LocalDateTime.now())")
    fun mediaDTOToEntity(dto: MediaDTO?): Media?

    @Named("preferencesDTOToPreferences")
    fun preferencesDTOToPreferences(preferencesDTO: PreferencesDTO?): Preferences?

    @Named("preferencesToPreferencesDTO")
    fun preferencesToPreferencesDTO(preferences: Preferences): PreferencesDTO

    @Named("patientProfileDTOToPatientProfile")
    fun patientProfileDTOToPatientProfile(profileDTO: PatientProfileDTO?): PatientProfile?

    @Named("patientProfileToPatientProfileDTO")
    fun patientProfileToPatientProfileDTO(profile: PatientProfile?): PatientProfileDTO?
}