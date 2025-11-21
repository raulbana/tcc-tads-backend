package br.ufpr.tads.daily_iu_services.adapter.input.admin

import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.*
import br.ufpr.tads.daily_iu_services.domain.service.AdminService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PatchMapping("/users/status")
    @Operation(summary = "Definir status do usuário (Ativo/Bloqueado)", description = "Ativa ou bloqueia um usuário")
    fun setUserStatus(
        @RequestHeader("x-user-id") adminId: Long,
        @RequestBody @Valid request: StatusAssignerDTO
    ): ResponseEntity<Void> {
        ResponseEntity.ok(adminService.setUserStatus(adminId, request))
        return ResponseEntity.noContent().build()
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
        return ResponseEntity.ok(adminService.getAllReports(page, size))
    }

    @PostMapping("/reports/validate")
    @Operation(summary = "Validar denúncia", description = "Marca uma denúncia como válida ou inválida")
    @ApiResponse(responseCode = "204", description = "Denúncia validada com sucesso")
    fun validateReport(@RequestBody @Valid request: ReportToggleDTO): ResponseEntity<Void> {
        adminService.validateReport(request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/reports/strike")
    @Operation(
        summary = "Aplicar advertência",
        description = "Aplica uma advertência a um usuário com base em uma publicação oculta"
    )
    @ApiResponse(responseCode = "204", description = "Advertência aplicada com sucesso")
    fun applyStrike(
        @RequestHeader("x-content-id") targetContentId: Long
    ): ResponseEntity<Void> {
        adminService.applyStrike(targetContentId)
        return ResponseEntity.noContent().build()
    }
}