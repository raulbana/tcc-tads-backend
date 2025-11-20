package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.ContentAdminDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.ContentReportAdminDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.RoleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.UserAdminViewDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.AuthorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentContentCategory
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentMedia
import br.ufpr.tads.daily_iu_services.domain.entity.content.Report
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.domain.entity.user.Role
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper
interface AdminMapper {

    companion object {
        val INSTANCE: AdminMapper = Mappers.getMapper(AdminMapper::class.java)

        @JvmStatic
        @Named("blockedToStatus")
        fun blockedToStatus(blocked: Boolean): String {
            return if (blocked) "Bloqueado" else "Ativo"
        }
    }

    @Mapping(target = "role", source = "role", qualifiedByName = ["toRoleDTO"])
    @Mapping(target = "status", source = "blocked", qualifiedByName = ["blockedToStatus"])
    fun toUserAdminViewDTO(user: User): UserAdminViewDTO

    @Named("toRoleDTO")
    fun toRoleDTO(role: Role): RoleDTO

    @Named("mediasToDTO")
    fun mediasToDTO(entities: List<ContentMedia>) = entities.map { mediaToDTO(it) }

    @Named("mediaToUrl")
    fun mediaToUrl(media: Media?) = media?.url

    @Named("userToAuthorDTO")
    @Mapping(target = "profilePicture", source = "profilePicture", qualifiedByName = ["mediaToUrl"])
    fun userToAuthorDTO(user: User): AuthorDTO

    @Mapping(target = "id", source = "media.id")
    @Mapping(target = "url", source = "media.url")
    @Mapping(target = "contentType", source = "media.contentType")
    @Mapping(target = "contentSize", source = "media.contentSize")
    @Mapping(target = "altText", source = "media.altText")
    @Mapping(target = "createdAt", source = "media.createdAt")
    fun mediaToDTO(entity: ContentMedia): MediaDTO

    @Mapping(target = "id", source = "content.id")
    @Mapping(target = "title", source = "content.title")
    @Mapping(target = "description", source = "content.description")
    @Mapping(target = "subtitle", source = "content.subtitle")
    @Mapping(target = "subcontent", source = "content.subcontent")
    @Mapping(target = "categories", source = "content.categories", qualifiedByName = ["contentContentCategoryToString"])
    @Mapping(target = "author", source = "content.author", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "media", source = "content.media", qualifiedByName = ["mediasToDTO"])
    @Mapping(target = "reports", source = "reports", qualifiedByName = ["reportsToContentReportAdminDTOs"])
    fun toContentAdminDTO(content: Content, reports: List<Report>): ContentAdminDTO

    @Named("reportsToContentReportAdminDTOs")
    fun reportsToContentReportAdminDTOs(reports: List<Report>): List<ContentReportAdminDTO> =
        reports.map { reportToContentReportAdminDTO(it) }

    @Named("contentContentCategoryToString")
    fun contentContentCategoryToString(category: ContentContentCategory): String = category.category.name

    @Named("reportToContentReportAdminDTO")
    fun reportToContentReportAdminDTO(report: Report): ContentReportAdminDTO
}