package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentRepostDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentUpdateDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ReportContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ToggleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper.ContentMapper
import br.ufpr.tads.daily_iu_services.adapter.output.content.CommentRepository
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
import java.util.PriorityQueue

@Service
class ContentService(
    private val contentRepository: ContentRepository,
    private val categoryRepository: ContentCategoryRepository,
    private val mediaRepository: MediaRepository,
    private val userRepository: UserRepository,
    private val savedContentRepository: SavedContentRepository,
    private val commentRepository: CommentRepository,
    private val reportsRepository: ContentReportsRepository,
    private val mailClient: MailClient
) {

    fun getContents(userId: Long, profile: Boolean, page: Int?, size: Int?): List<ContentSimpleDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)

        if (profile) {
            val contents = contentRepository.findByAuthorIdAndStrikedFalse(userId, pageable)
            return contents.map { ContentMapper.INSTANCE.contentToSimpleDTO(it) }
        }

        val recommended = contentRepository.findRecommendedByUserLikes(userId, pageable)
        val sectionsMap = mutableMapOf<Long, MutableList<String>>()

        // otimização: uma passagem para coletar top-K usando heaps e popular o mapa de seções
        val contents = recommended.content
        val k = (contents.size * 0.3).toInt().coerceAtLeast(1)

        val likesHeap = PriorityQueue<Content>(compareBy { it.likes.size })
        val commentsHeap = PriorityQueue<Content>(compareBy { it.comments.size })
        val recentHeap = PriorityQueue<Content>(compareBy { it.createdAt })
        val trendingScore: (Content) -> Double = { c ->
            val ageInHours =
                java.time.Duration.between(c.createdAt, LocalDateTime.now()).toHours().toDouble().coerceAtLeast(1.0)
            (c.likes.size + c.comments.size).toDouble() / ageInHours
        }
        val trendingHeap = PriorityQueue(compareBy(trendingScore))

        for (content in contents) {
            val id = content.id!!
            sectionsMap[id] = sectionsMap.getOrDefault(id, mutableListOf())
            likesHeap.add(content); if (likesHeap.size > k) likesHeap.poll()
            commentsHeap.add(content); if (commentsHeap.size > k) commentsHeap.poll()
            recentHeap.add(content); if (recentHeap.size > k) recentHeap.poll()
            trendingHeap.add(content); if (trendingHeap.size > k) trendingHeap.poll()
        }

        while (likesHeap.isNotEmpty()) {
            val c = likesHeap.poll()
            sectionsMap[c.id!!]?.add("Mais curtidas")
        }
        while (commentsHeap.isNotEmpty()) {
            val c = commentsHeap.poll()
            sectionsMap[c.id!!]?.add("Mais comentadas")
        }
        while (recentHeap.isNotEmpty()) {
            val c = recentHeap.poll()
            sectionsMap[c.id!!]?.add("Mais recentes")
        }
        while (trendingHeap.isNotEmpty()) {
            val c = trendingHeap.poll()
            sectionsMap[c.id!!]?.add("Em alta")
        }

        // você também pode gostar = conteúdos sem nenhuma seção
        for ((_, sections) in sectionsMap) {
            if (sections.isEmpty()) {
                sections.add("Você também pode gostar")
            }
        }

        return recommended.content.map { ContentMapper.INSTANCE.contentToSimpleDTO(it, sectionsMap[it.id]) }
    }

    fun getContentById(id: Long, userId: Long): ContentDTO {
        val content = contentRepository.findByIdAndStrikedFalse(id)
            ?: throw NotFoundException("Conteúdo com id $id não encontrado")

        val saved = savedContentRepository.existsByUserIdAndContentId(userId, content.id!!)
        val totalComments = commentRepository.countByContentIdAndReplyFalse(id)
        content.comments.clear()
        content.comments.addAll(commentRepository.findByContentIdAndReplyFalse(id, PageRequest.of(0, 20)))

        return ContentMapper.INSTANCE.contentToDTO(content, userId, saved, totalComments)
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
        val existingContent = contentRepository.findByIdAndStrikedFalse(contentId)
            ?: throw NotFoundException("Conteúdo com id $contentId não encontrado")

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

        val saved = savedContentRepository.existsByUserIdAndContentId(userId, existingContent.id!!)
        val result = contentRepository.save(existingContent)
        val totalComments = commentRepository.countByContentIdAndReplyFalse(contentId)
        result.comments.clear()
        result.comments.addAll(commentRepository.findByContentIdAndReplyFalse(contentId, PageRequest.of(0, 20)))
        return ContentMapper.INSTANCE.contentToDTO(result, userId, saved, totalComments)
    }

    fun deleteContent(contentId: Long) {
        val existingContent = contentRepository.findByIdAndStrikedFalse(contentId)
            ?: throw NotFoundException("Conteúdo com id $contentId não encontrado")

        existingContent.visible = false
        existingContent.striked = true
        contentRepository.save(existingContent)
    }

    fun repostContent(contentId: Long, request: ContentRepostDTO): ContentDTO {
        val originalContent = contentRepository.findByIdAndStrikedFalse(contentId)
            ?: throw NotFoundException("Conteúdo com id $contentId não encontrado")

        if (originalContent.author.id == request.repostedByUserId) {
            throw NotAllowedException("Usuário não pode repostar seu próprio conteúdo")
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
        val content = contentRepository.findByIdAndStrikedFalse(contentId)
            ?: throw NotFoundException("Conteúdo com id $contentId não encontrado")

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
        val content = contentRepository.findByIdAndStrikedFalse(contentId)
            ?: throw NotFoundException("Conteúdo com id $contentId não encontrado")

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
        val content = contentRepository.findByIdAndStrikedFalse(contentId)
            ?: throw NotFoundException("Conteúdo com id $contentId não encontrado")

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

    fun getUserContentStats(userId: Long): Map<String, Long> {
        val totalContents = contentRepository.countByAuthorIdAndStrikedFalse(userId)
        val totalLikes = contentRepository.countTotalLikesByAuthorIdAndStrikedFalse(userId) ?: 0L
        val totalSaves = savedContentRepository.countByUserIdAndContent_StrikedFalse(userId) ?: 0L

        return mapOf (
            "postagens" to totalContents,
            "curtidas" to totalLikes,
            "salvos" to totalSaves
        )
    }
}