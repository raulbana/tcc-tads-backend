package br.ufpr.tads.daily_iu_services.adapter.input.user

import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginResponseDTO
import br.ufpr.tads.daily_iu_services.domain.service.UserService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser() {
        TODO("Not yet implemented")
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        return ResponseEntity.ok(userService.login(request))
    }

    @PostMapping("/password/reset")
    fun resetPassword(@RequestHeader("x-user-email") @Email email: String) {
        TODO("Not yet implemented")
    }
}