package br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.AuthorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CategoryDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.domain.entity.content.Category
import br.ufpr.tads.daily_iu_services.domain.entity.content.Comment
import br.ufpr.tads.daily_iu_services.domain.entity.content.CommentLikes
import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentLikes
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentMedia
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

@Mapper
abstract class ContentMapper {

    companion object {
        val INSTANCE: ContentMapper = Mappers.getMapper(ContentMapper::class.java)
    }

    @Mapping(target = "author", source = "author", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "cover", source = "media", qualifiedByName = ["getCoverMedia"])
    @Mapping(target = "category", source = "category", qualifiedByName = ["categoryToString"])
    @Mapping(target= "isReposted", source = "repost")
    abstract fun contentToSimpleDTO(entity: Content): ContentSimpleDTO

    @Mapping(target = "id", source = "content.id")
    @Mapping(target = "title", source = "content.title")
    @Mapping(target = "description", source = "content.description")
    @Mapping(target = "subtitle", source = "content.subtitle")
    @Mapping(target = "subcontent", source = "content.subcontent")
    @Mapping(target = "category", source = "content.category", qualifiedByName = ["categoryToString"])
    @Mapping(target = "author", source = "content.author", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "cover", source = "content.media", qualifiedByName = ["getCoverMedia"])
    @Mapping(target = "media", source = "content.media", qualifiedByName = ["mediasToDTO"])
    @Mapping(target = "isLiked", expression = "java(getContentLikeStatus(content.getLikes(), userId))")
    @Mapping(target = "likesCount", source = "content.likes", qualifiedByName = ["countSize"])
    @Mapping(target = "comments", expression = ("java(commentsToDTO(content.getComments(), userId))"))
    @Mapping(target = "commentsCount", source = "content.comments", qualifiedByName = ["countSize"])
    @Mapping(target = "isReposted", source = "content.repost")
    @Mapping(target = "repostedFromContentId", source = "content.repostFromContentId")
    @Mapping(target = "repostedByUser", source = "content.repostByAuthor", qualifiedByName = ["userToAuthorDTO"])
    @Mapping(target = "createdAt", source = "content.createdAt")
    @Mapping(target = "updatedAt", source = "content.updatedAt")
    abstract fun contentToDTO(content: Content, userId: Long): ContentDTO

    @Named("userToAuthorDTO")
    @Mapping(target = "profilePicture", source = "profilePictureUrl")
    abstract fun userToAuthorDTO(user: User): AuthorDTO

    @Named("getCoverMedia")
    fun getCoverMedia(mediaList: List<ContentMedia>) = mediaList.map { mediaToDTO(it) }.firstOrNull()

    @Named("categoryToString")
    fun categoryToString(category: Category) = category.name

    @Named("countSize")
    fun countSize(list: List<Any>) = list.size

    @Named("mediasToDTO")
    fun mediasToDTO(entities: List<ContentMedia>) = entities.map { mediaToDTO(it) }
    
    @Named("commentsToDTO")
    fun commentsToDTO(entities: List<Comment>, userId: Long) = entities.map { commentToDTO(it, userId) }
    
    @Named("getContentLikeStatus")
    fun getContentLikeStatus(likes: List<ContentLikes>, userId: Long) = likes.any { it.userId == userId}

    @Named("getCommentLikeStatus")
    fun getCommentLikeStatus(likes: List<CommentLikes>, userId: Long) = likes.any { it.userId == userId}

    @Mapping(target = "url", source = "media.url")
    @Mapping(target = "contentType", source = "media.contentType")
    @Mapping(target = "contentSize", source = "media.contentSize")
    @Mapping(target = "altText", source = "media.altText")
    @Mapping(target = "createdAt", source = "media.createdAt")
    abstract fun mediaToDTO(entity: ContentMedia): MediaDTO

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", constant = "0l")
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

    abstract fun categoryToDTO(entity: Category): CategoryDTO

    abstract fun categoryDTOToEntity(dto: CategoryDTO): Category
}