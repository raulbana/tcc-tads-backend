package br.ufpr.tads.daily_iu_services.adapter.input.user

import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.ChangePasswordDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginResponseDTO
import br.ufpr.tads.daily_iu_services.domain.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
@Tag(name = "User", description = "Endpoints for managing user authentication and password recovery")
class UserController(private val userService: UserService) {

    @PostMapping
    @Operation(summary = "Create User", description = "Create a new user account")
    fun createUser() {
        TODO("Not yet implemented")
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate a user and return a JWT token with user details")
    fun login(@RequestBody @Valid request: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        return ResponseEntity.ok(userService.login(request))
    }

    @PostMapping("/password/forgot")
    @Operation(summary = "Initiate Password Reset", description = "Send an OTP to the user's email for password reset")
    @ApiResponse(responseCode = "204", description = "OTP sent successfully")
    fun resetPassword(@RequestHeader("x-user-email") @Email email: String): ResponseEntity<Void> {
        userService.sendEmailOTP(email)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/password/reset")
    @Operation(summary = "Change Password", description = "Change the user's password using the OTP sent to their email")
    @ApiResponse(responseCode = "204", description = "Password changed successfully")
    fun changePassword(@RequestBody @Valid request: ChangePasswordDTO): ResponseEntity<Void> {
        userService.resetPassword(request)
        return ResponseEntity.noContent().build()
    }
}