package br.ufpr.tads.daily_iu_services.adapter.input.admin

import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.ContentAdminDTO
import br.ufpr.tads.daily_iu_services.adapter.input.admin.dto.ReportToggleDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
class AdminController {

    @GetMapping("/users")
    @Operation(summary = "Listar usuários", description = "Recupera uma lista de todos os usuários")
    fun getUsers(@RequestHeader("x-user-id") userId: Long) {
        TODO("Not implemented yet")
    }

    @PostMapping("/users/role")
    @Operation(summary = "Definir papel do usuário", description = "Define ou altera o papel de um usuário")
    fun setUserRole(@RequestHeader("x-user-id") userId: Long) {
        TODO("Not implemented yet")
    }

    @GetMapping("/reports")
    @Operation(summary = "Listar denúncias", description = "Recupera uma lista de todas as denúncias, agrupadas por conteúdo")
    fun getReports(@RequestHeader("x-user-id") userId: Long): ResponseEntity<List<ContentAdminDTO>> {
        TODO("Not implemented yet")
    }

    @PostMapping("/reports/validate")
    @Operation(summary = "Validar denúncia", description = "Marca uma denúncia como válida ou inválida")
    @ApiResponse(responseCode = "204", description = "Denúncia validada com sucesso")
    fun validateReport(@RequestHeader("x-user-id") userId: Long, @RequestBody request: ReportToggleDTO): ResponseEntity<Void> {
        TODO("Not implemented yet")
    }

    @PostMapping("/reports/strike")
    @Operation(summary = "Aplicar advertência", description = "Aplica uma advertência a um usuário com base em uma publicação oculta")
    @ApiResponse(responseCode = "204", description = "Advertência aplicada com sucesso")
    fun applyStrike(@RequestHeader("x-user-id") userId: Long, @RequestHeader("x-target-id") targetContentId: Long): ResponseEntity<Void> {
        TODO("Not implemented yet")
    }
}