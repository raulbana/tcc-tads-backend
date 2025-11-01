package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.ContentAdminDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.RoleAssignerDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.UserAdminViewDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.mapper.AdminMapper
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentReportsRepository
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentRepository
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
    private val contentReportsRepository: ContentReportsRepository
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
            documentType =  request.documentType,
            documentValue = request.documentValue,
            conceivedBy = admin,
            conceivedAt = LocalDateTime.now()
        )

        targetUser.role = newRole
        val updatedUser = userRepository.save(targetUser)

        return AdminMapper.INSTANCE.toUserAdminViewDTO(updatedUser)
    }

    fun getAllReports(page: Int?, size: Int?): List<ContentAdminDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)
        val flaggedContents = contentRepository.findByVisibleFalse(pageable)

        val reportsByContent = flaggedContents.associateWith {
            val reports = contentReportsRepository.findByContentIdAndHandledFalse(it.id!!)
            reports
        }

        return reportsByContent.map {
            AdminMapper.INSTANCE.toContentAdminDTO(it.key, it.value)
        }
    }
}