package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentRepostDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentUpdateDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ToggleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper.ContentMapper
import br.ufpr.tads.daily_iu_services.adapter.output.content.CategoryRepository
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentRepository
import br.ufpr.tads.daily_iu_services.adapter.output.media.MediaRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentLikes
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentMedia
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContentService(
    private val contentRepository: ContentRepository,
    private val categoryRepository: CategoryRepository,
    private val mediaRepository: MediaRepository,
    private val userRepository: UserRepository
) {

    fun getContents(userId: Long): List<ContentSimpleDTO> {
        // TODO: For now, we are not using the userId, but it can be used for personalized content in the future
        val contents = contentRepository.findAll()

        return contents.map { ContentMapper.INSTANCE.contentToSimpleDTO(it) }
    }

    fun getContentById(id: Long, userId: Long): ContentDTO {
        val content = contentRepository.findById(id).orElseThrow {
            throw NotFoundException("Conteúdo com id $id não encontrado")
        }

        content.comments.sortBy { it.createdAt }
        if (content.comments.size > 15) {
            content.comments.removeAll { it != content.comments.take(15) }
        }

        return ContentMapper.INSTANCE.contentToDTO(content, userId)
    }

    fun createContent(request: ContentCreatorDTO): ContentDTO {
        val category = categoryRepository.findById(request.categoryId).orElseThrow {
            throw NotFoundException("Categoria com id ${request.categoryId} não encontrada")
        }

        val author = userRepository.findById(request.authorId).orElseThrow {
            throw NotFoundException("Usuário com id ${request.authorId} não encontrado")
        }

        val content = Content(
            title = request.title,
            description = request.description,
            subtitle = request.subtitle,
            subcontent = request.subcontent,
            category = category,
            author = author,
            createdAt = LocalDateTime.now()
        )

        val medias = request.media
            .map { ContentMapper.INSTANCE.mediaDTOToEntity(it) }
            .map { mediaRepository.save(it) }
            .map { ContentMedia(content = content, media = it) }

        content.media.addAll(medias)

        if (category.auditable) {
            TODO("Implementar lógica de auditoria para categorias que necessitam de aprovação")
        } else {
            val result = contentRepository.save(content)
            return ContentMapper.INSTANCE.contentToDTO(result, request.authorId)
        }
    }

    fun updateContent(request: ContentUpdateDTO, contentId: Long, userId: Long): ContentDTO {
        val existingContent = contentRepository.findById(contentId).orElseThrow {
            throw NotFoundException("Conteúdo com id $contentId não encontrado")
        }

        val updatedContent: Content = existingContent.copy(
            title = request.title,
            description = request.description,
            subtitle = request.subtitle,
            subcontent = request.subcontent
        )

        syncContentMediaList(
            updatedContent.media,
            request.media.map { ContentMapper.INSTANCE.mediaDTOToEntity(it) }
        )

        val result = contentRepository.save(updatedContent)

        return ContentMapper.INSTANCE.contentToDTO(result, userId)
    }

    fun syncContentMediaList(
        contentMediaList: MutableList<ContentMedia>,
        mediaList: List<Media>
    ) {
        mediaList.forEach { media ->
            val persistedMedia = if (media.id == null) mediaRepository.save(media) else media

            val exists = contentMediaList.any { it.media == persistedMedia }
            if (!exists) {
                val content = contentMediaList.firstOrNull()?.content
                if (content != null) {
                    contentMediaList.add(ContentMedia(content = content, media = persistedMedia))
                }
            }
        }

        val mediaSet = mediaList.toSet()
        contentMediaList.removeIf { !mediaSet.contains(it.media) }
    }

    fun deleteContent(contentId: Long) {
        val existingContent = contentRepository.findById(contentId).orElseThrow {
            throw NotFoundException("Conteúdo com id $contentId não encontrado")
        }

        contentRepository.delete(existingContent)
    }

    fun repostContent(contentId: Long, request: ContentRepostDTO): ContentDTO {
        val originalContent = contentRepository.findById(contentId).orElseThrow {
            throw NotFoundException("Conteúdo com id $contentId não encontrado")
        }

        val repostByUser = userRepository.findById(request.repostedByUserId).orElseThrow {
            throw NotFoundException("Usuário com id ${request.repostedByUserId} não encontrado")
        }

        val repostedContent = originalContent.copy(
            id = null,
            repost = true,
            repostFromContentId = originalContent.id,
            repostByAuthor = repostByUser,
            likes = mutableListOf(),
            comments = mutableListOf(),
            media = mutableListOf()
        )

        val repostMedias = originalContent.media.map { originalContentMedia ->
            val media = originalContentMedia.media
            ContentMedia(content = repostedContent, media = media)
        }
        repostedContent.media.addAll(repostMedias)

        val result = contentRepository.save(repostedContent)
        return ContentMapper.INSTANCE.contentToDTO(result, request.repostedByUserId)
    }

    fun toggleLikeContent(contentId: Long, toggle: ToggleDTO) {
        val content = contentRepository.findById(contentId).orElseThrow {
            throw NotFoundException("Conteúdo com id $contentId não encontrado")
        }

        if (toggle.control) {
            val alreadyLiked = content.likes.any { it.userId == toggle.userId }
            if (!alreadyLiked) {
                content.likes.add(ContentLikes(content = content, userId = toggle.userId))
                contentRepository.save(content)
            }
        } else {
            val like = content.likes.firstOrNull { it.userId == toggle.userId }
            if (like != null) {
                content.likes.remove(like)
                contentRepository.save(content)
            }
        }
    }
}