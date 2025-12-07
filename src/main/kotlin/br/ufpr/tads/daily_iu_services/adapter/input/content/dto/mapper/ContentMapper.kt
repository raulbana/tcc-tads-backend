package br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.AuthorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCategoryDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.domain.entity.content.Category
import br.ufpr.tads.daily_iu_services.domain.entity.content.Comment
import br.ufpr.tads.daily_iu_services.domain.entity.content.CommentLikes
import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentContentCategory
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentLikes
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentMedia
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper
abstract class ContentMapper {

    companion object {
        val INSTANCE: ContentMapper = Mappers.getMapper(ContentMapper::class.java)
    }

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "title", source = "entity.title")
    @Mapping(target = "categories", source = "entity.categories", qualifiedByName = ["categoriesToString"])
    @Mapping(target = "author", source = "entity.author", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "cover", source = "entity.media", qualifiedByName = ["getCoverMedia"])
    @Mapping(target = "section", source = "section")
    @Mapping(target = "isReposted", source = "entity.repost")
    @Mapping(target = "createdAt", source = "entity.createdAt")
    @Mapping(target = "updatedAt", source = "entity.updatedAt")
    abstract fun contentToSimpleDTO(entity: Content, section: List<String>? = null): ContentSimpleDTO

    @Mapping(target = "id", source = "content.id")
    @Mapping(target = "title", source = "content.title")
    @Mapping(target = "description", source = "content.description")
    @Mapping(target = "subtitle", source = "content.subtitle")
    @Mapping(target = "subcontent", source = "content.subcontent")
    @Mapping(target = "categories", source = "content.categories", qualifiedByName = ["categoriesToString"])
    @Mapping(target = "author", source = "content.author", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "cover", source = "content.media", qualifiedByName = ["getCoverMedia"])
    @Mapping(target = "media", source = "content.media", qualifiedByName = ["mediasToDTO"])
    @Mapping(target = "isSaved", source = "saved")
    @Mapping(target = "isLiked", expression = "java(getContentLikeStatus(content.getLikes(), userId))")
    @Mapping(target = "likesCount", source = "content.likes", qualifiedByName = ["countSize"])
    @Mapping(target = "comments", expression = ("java(commentsToDTO(content.getComments(), userId))"))
    @Mapping(target = "commentsCount", source = "totalComments")
    @Mapping(target = "isReposted", source = "content.repost")
    @Mapping(target = "repostedFromContentId", source = "content.repostFromContentId")
    @Mapping(target = "repostedByUser", source = "content.repostByAuthor", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "createdAt", source = "content.createdAt")
    @Mapping(target = "updatedAt", source = "content.updatedAt")
    abstract fun contentToDTO(content: Content, userId: Long, saved: Boolean = false, totalComments: Int = 0): ContentDTO

    @Named("mediaToUrl")
    fun mediaToUrl(media: Media?) = media?.url

    @Named("userToAuthorDTO")
    @Mapping(target = "profilePicture", source = "profilePicture", qualifiedByName = ["mediaToUrl"])
    abstract fun userToAuthorDTO(user: User): AuthorDTO

    @Named("getCoverMedia")
    fun getCoverMedia(mediaList: List<ContentMedia>) = mediaList
        .filter { it.media.contentType.contains("image") }
        .map { mediaToDTO(it) }.firstOrNull()

    @Named("categoriesToString")
    fun categoriesToString(categories: List<ContentContentCategory>) = categories.map { it.category.name }

    @Named("countSize")
    fun countSize(list: List<Any>) = list.size

    @Named("mediasToDTO")
    fun mediasToDTO(entities: List<ContentMedia>) = entities.map { mediaToDTO(it) }

    @Named("commentsToDTO")
    fun commentsToDTO(entities: List<Comment>, userId: Long) = entities.map { commentToDTO(it, userId) }

    @Named("getContentLikeStatus")
    fun getContentLikeStatus(likes: List<ContentLikes>, userId: Long) = likes.any { it.userId == userId }

    @Named("getCommentLikeStatus")
    fun getCommentLikeStatus(likes: List<CommentLikes>, userId: Long) = likes.any { it.userId == userId }

    @Mapping(target = "id", source = "media.id")
    @Mapping(target = "url", source = "media.url")
    @Mapping(target = "contentType", source = "media.contentType")
    @Mapping(target = "contentSize", source = "media.contentSize")
    @Mapping(target = "altText", source = "media.altText")
    @Mapping(target = "createdAt", source = "media.createdAt")
    abstract fun mediaToDTO(entity: ContentMedia): MediaDTO

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    abstract fun mediaDTOToEntity(dto: MediaDTO): Media

    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "author", source = "comment.author", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "text", source = "comment.text")
    @Mapping(target = "createdAt", source = "comment.createdAt")
    @Mapping(target = "updatedAt", source = "comment.updatedAt")
    @Mapping(target = "likesCount", source = "comment.likes", qualifiedByName = ["countSize"])
    @Mapping(target = "isLiked", expression = "java(getCommentLikeStatus(comment.getLikes(), userId))")
    @Mapping(target = "repliesCount", source = "comment.replies", qualifiedByName = ["countSize"])
    abstract fun commentToDTO(comment: Comment, userId: Long): CommentDTO

    abstract fun categoryToDTO(entity: Category): ContentCategoryDTO

    abstract fun categoryDTOToEntity(dto: ContentCategoryDTO): Category
}