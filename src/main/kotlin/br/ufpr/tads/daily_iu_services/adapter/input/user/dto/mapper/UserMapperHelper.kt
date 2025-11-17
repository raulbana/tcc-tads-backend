package br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper

import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.domain.entity.user.Role
import br.ufpr.tads.daily_iu_services.domain.entity.user.UserPermissionEnum
import org.mapstruct.Named

object UserMapperHelper {
    @JvmStatic
    @Named("mediaToUrl")
    fun mediaToUrl(media: Media?): String? = media?.url

    @JvmStatic
    @Named("roleToString")
    fun roleToString(role: Role) = UserPermissionEnum.fromLevel(role.permissionLevel).label
}