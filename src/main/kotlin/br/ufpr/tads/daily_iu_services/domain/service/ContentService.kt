package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentRepostDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentUpdateDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ReportContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ToggleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper.ContentMapper
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentCategoryRepository
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentReportsRepository
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentRepository
import br.ufpr.tads.daily_iu_services.adapter.output.content.SavedContentRepository
import br.ufpr.tads.daily_iu_services.adapter.output.media.MediaRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.MailClient
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentContentCategory
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentLikes
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentMedia
import br.ufpr.tads.daily_iu_services.domain.entity.content.Report
import br.ufpr.tads.daily_iu_services.domain.entity.content.SavedContent
import br.ufpr.tads.daily_iu_services.exception.NotAllowedException
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContentService (
    private val contentRepository: ContentRepository,
    private val categoryRepository: ContentCategoryRepository,
    private val mediaRepository: MediaRepository,
    private val userRepository: UserRepository,
    private val savedContentRepository: SavedContentRepository,
    private val reportsRepository: ContentReportsRepository,
    private val mailClient: MailClient
) {

    fun getContents(userId: Long, profile: Boolean, page: Int?, size: Int?): List<ContentSimpleDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)

        if (profile) {
            val contents = contentRepository.findByAuthorId(userId, pageable)
            return contents.map { ContentMapper.INSTANCE.contentToSimpleDTO(it) }
        }

        val recommended = contentRepository.findRecommendedByUserLikes(userId, pageable)
        return recommended.content.map { ContentMapper.INSTANCE.contentToSimpleDTO(it) }
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
        var auditable = false
        val author = userRepository.findById(request.authorId).orElseThrow {
            throw NotFoundException("Usuário com id ${request.authorId} não encontrado")
        }

        val content = Content(
            title = request.title,
            description = request.description,
            subtitle = request.subtitle,
            subcontent = request.subcontent,
            author = author,
            createdAt = LocalDateTime.now()
        )

        val category = request.categoryIds.map {
            categoryRepository.findById(it).orElseThrow {
                throw NotFoundException("Categoria com id $it não encontrada")
            }
        }.map {
            if (it.auditable) {
                auditable = true
            }
            ContentContentCategory(content = content, category = it)
        }.toMutableList()

        val medias = request.media
            .map { ContentMapper.INSTANCE.mediaDTOToEntity(it) }
            .map { if (it.id == null) mediaRepository.save(it) else it }
            .map { ContentMedia(content = content, media = it) }

        content.media.addAll(medias)
        content.categories.addAll(category)

        if (auditable) {
            val authorPermission = author.role.permissionLevel
            if (authorPermission < 2) {
                throw NotAllowedException("Usuário com id ${request.authorId} não tem permissão para criar conteúdo auditável")
            } else {
                //content.visible = false
                val result = contentRepository.save(content)
                //mailClient.sendContentForAudit(result)
                return ContentMapper.INSTANCE.contentToDTO(result, request.authorId)
            }
        } else {
            val result = contentRepository.save(content)
            return ContentMapper.INSTANCE.contentToDTO(result, request.authorId)
        }
    }

    fun updateContent(request: ContentUpdateDTO, contentId: Long, userId: Long): ContentDTO {
        val existingContent = contentRepository.findById(contentId).orElseThrow {
            throw NotFoundException("Conteúdo com id $contentId não encontrado")
        }

        existingContent.title = request.title
        existingContent.description = request.description
        existingContent.subtitle = request.subtitle
        existingContent.subcontent = request.subcontent
        existingContent.media.clear()

        val medias = request.media
            .map { ContentMapper.INSTANCE.mediaDTOToEntity(it) }
            .map { if (it.id == null) mediaRepository.save(it) else it }
            .map { ContentMedia(content = existingContent, media = it) }

        existingContent.media.addAll(medias)

        val result = contentRepository.save(existingContent)
        return ContentMapper.INSTANCE.contentToDTO(result, userId)
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

    fun toggleSaveContent(contentId: Long, toggle: ToggleDTO) {
        val content = contentRepository.findById(contentId).orElseThrow {
            throw NotFoundException("Conteúdo com id $contentId não encontrado")
        }
        if (toggle.control) {
            val alreadySaved = savedContentRepository.findByUserIdAndContentId(toggle.userId, contentId)
            if (alreadySaved == null) {
                savedContentRepository.save(
                    SavedContent(
                        content = content,
                        user = userRepository.findById(toggle.userId).orElseThrow {
                            throw NotFoundException("Usuário com id ${toggle.userId} não encontrado")
                        }
                    )
                )
            }
        } else {
            val savedContent = savedContentRepository.findByUserIdAndContentId(toggle.userId, contentId)
            if (savedContent != null) {
                savedContentRepository.delete(savedContent)
            }
        }
    }

    fun listSavedContents(userId: Long): List<ContentSimpleDTO> {
        val savedContents = savedContentRepository.findByUserIdAndContent_VisibleTrue(userId)
        return savedContents.map { ContentMapper.INSTANCE.contentToSimpleDTO(it.content) }
    }

    fun reportContent(contentId: Long, request: ReportContentDTO) {
        val content = contentRepository.findById(contentId).orElseThrow {
            throw NotFoundException("Conteúdo com id $contentId não encontrado")
        }
        val reportedByUser = userRepository.findById(request.reporterId).orElseThrow {
            throw NotFoundException("Usuário com id ${request.reporterId} não encontrado")
        }

        if (!reportsRepository.existsByContentIdAndReportedByUserIdAndValidTrue(contentId, request.reporterId)) {
            val report = Report(
                content = content,
                reportedByUser = reportedByUser,
                reason = request.reason,
                createdAt = LocalDateTime.now()
            )

            reportsRepository.save(report)

            // ***************** Início da lógica de AÇÃO após certos limites de reports *****************
            val contentReportThreshold = 5L

            val reportCount = reportsRepository.countByContentIdAndValidTrue(contentId)
            if (reportCount >= contentReportThreshold) {
                mailClient.sendContentShutdownWarning(content, reportCount)

                content.visible = false
                contentRepository.save(content)
            }
        }
    }
}