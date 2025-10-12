package br.ufpr.tads.daily_iu_services.adapter.input.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/admin")
@Tag(name = "Admin", description = "Endpoints para tarefas administrativas")
class AdminController {

    @GetMapping("/users")
    @Operation(summary = "Listar usuários", description = "Recupera uma lista de todos os usuários")
    fun getUsers() {
        TODO("Not implemented yet")
    }

    @PostMapping("/users/role")
    @Operation(summary = "Definir papel do usuário", description = "Define ou altera o papel de um usuário")
    fun setUserRole() {
        TODO("Not implemented yet")
    }
}