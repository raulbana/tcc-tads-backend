package br.ufpr.tads.daily_iu_services.adapter.input.admin

import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.ContentAdminDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.ReportToggleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.RoleAssignerDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.UserAdminViewDTO
import br.ufpr.tads.daily_iu_services.domain.service.AdminService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/admin")
@Tag(name = "Admin", description = "Endpoints para tarefas administrativas")
class AdminController(private val adminService: AdminService) {

    @GetMapping("/users")
    @Operation(summary = "Listar usuários", description = "Recupera uma lista de todos os usuários")
    fun getUsers(
        @RequestHeader("page", required = false) page: Int?,
        @RequestHeader("size", required = false) size: Int?
    ): ResponseEntity<List<UserAdminViewDTO>> {
        return ResponseEntity.ok(adminService.getAllUsers(page, size))
    }

    @PostMapping("/users/role")
    @Operation(summary = "Definir papel do usuário", description = "Define ou altera o papel de um usuário")
    fun setUserRole(
        @RequestHeader("x-user-id") adminId: Long,
        @RequestBody @Valid request: RoleAssignerDTO
    ): ResponseEntity<UserAdminViewDTO> {
        return ResponseEntity.ok(adminService.setUserRole(adminId, request))
    }

    @GetMapping("/reports")
    @Operation(
        summary = "Listar denúncias",
        description = "Recupera uma lista de todas as denúncias, agrupadas por conteúdo"
    )
    fun getReports(
        @RequestHeader("page", required = false) page: Int?,
        @RequestHeader("size", required = false) size: Int?,
    ): ResponseEntity<List<ContentAdminDTO>> {
        TODO("Not implemented yet")
    }

    @PostMapping("/reports/validate")
    @Operation(summary = "Validar denúncia", description = "Marca uma denúncia como válida ou inválida")
    @ApiResponse(responseCode = "204", description = "Denúncia validada com sucesso")
    fun validateReport(
        @RequestHeader("x-user-id") userId: Long,
        @RequestBody request: ReportToggleDTO
    ): ResponseEntity<Void> {
        TODO("Not implemented yet")
    }

    @PostMapping("/reports/strike")
    @Operation(
        summary = "Aplicar advertência",
        description = "Aplica uma advertência a um usuário com base em uma publicação oculta"
    )
    @ApiResponse(responseCode = "204", description = "Advertência aplicada com sucesso")
    fun applyStrike(
        @RequestHeader("x-user-id") userId: Long,
        @RequestHeader("x-target-id") targetContentId: Long
    ): ResponseEntity<Void> {
        TODO("Not implemented yet")
    }
}