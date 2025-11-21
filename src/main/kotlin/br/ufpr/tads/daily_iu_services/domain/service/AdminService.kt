package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.*
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.mapper.AdminMapper
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentReportsRepository
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.MailClient
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.domain.entity.user.Role
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminService(
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository,
    private val contentReportsRepository: ContentReportsRepository,
    private val mailClient: MailClient
) {

    fun getAllUsers(page: Int?, size: Int?): List<UserAdminViewDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)

        val users = userRepository.findAll(pageable).content

        return users.map { AdminMapper.INSTANCE.toUserAdminViewDTO(it) }
    }

    fun setUserRole(adminId: Long, request: RoleAssignerDTO): UserAdminViewDTO {
        val admin = userRepository.findById(adminId).orElseThrow {
            NotFoundException("Usuário administrador com ID $adminId não encontrado.")
        }

        val targetUser = userRepository.findById(request.targetUserId).orElseThrow {
            NotFoundException("Usuário alvo com ID ${request.targetUserId} não encontrado.")
        }

        val newRole = Role(
            description = request.description,
            permissionLevel = request.permissionLevel,
            reason = request.reason,
            hasDocument = request.hasDocument,
            documentType = request.documentType,
            documentValue = request.documentValue,
            conceivedBy = admin,
            conceivedAt = LocalDateTime.now()
        )

        targetUser.role = newRole
        val updatedUser = userRepository.save(targetUser)

        return AdminMapper.INSTANCE.toUserAdminViewDTO(updatedUser)
    }

    fun setUserStatus(adminId: Long, request: StatusAssignerDTO) {
        val admin = userRepository.findById(adminId).orElseThrow {
            NotFoundException("Usuário administrador com ID $adminId não encontrado.")
        }

        val targetUser = userRepository.findById(request.targetUserId).orElseThrow {
            NotFoundException("Usuário alvo com ID ${request.targetUserId} não encontrado.")
        }

        targetUser.blocked = request.blocked
        userRepository.save(targetUser)
    }

    fun getAllReports(page: Int?, size: Int?): List<ContentAdminDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)
        val flaggedContents = contentRepository.findByVisibleFalseAndStrikedFalse(pageable)

        val reportsByContent = flaggedContents.associateWith {
            val reports = contentReportsRepository.findByContentIdAndHandledFalse(it.id!!)
            reports
        }

        return reportsByContent.map {
            AdminMapper.INSTANCE.toContentAdminDTO(it.key, it.value)
        }
    }

    fun validateReport(request: ReportToggleDTO) {
        val report = contentReportsRepository.findById(request.reportId).orElseThrow {
            NotFoundException("Denúncia com ID ${request.reportId} não encontrada.")
        }
        report.handled = true

        if (!request.valid) {
            val reportThreshold = 5L
            report.valid = false
            val reportsCount = contentReportsRepository.countByContentIdAndValidTrue(request.contentId)

            if (reportsCount - 1 < reportThreshold) {
                val content = contentRepository.findById(request.contentId).orElseThrow {
                    NotFoundException("Conteúdo com ID ${request.contentId} não encontrado.")
                }
                content.visible = true
                contentRepository.save(content)
            }
        }
        contentReportsRepository.save(report)
    }

    fun applyStrike(targetContentId: Long) {
        val content = contentRepository.findById(targetContentId).orElseThrow {
            NotFoundException("Conteúdo com ID $targetContentId não encontrado.")
        }

        content.striked = true
        contentRepository.save(content)

        val author = content.author
        author.strikes += 1
        userRepository.save(author)

        // Validação de limite de strikes
        val strikesThreshold = 3
        if (author.strikes >= strikesThreshold) {
            author.blocked = true
            userRepository.save(author)
        }

        // Notificação de usuário
        if (author.blocked) {
            mailClient.sendUserBanNotification(author.email)
        } else {
            mailClient.sendUserBanWarning(author.email, content.title, author.strikes)
        }
    }
}