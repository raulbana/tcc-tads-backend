package br.ufpr.tads.daily_iu_services.adapter.input.user

import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.ExerciseFeedbackCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.ChangePasswordDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginResponseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserEditorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.WorkoutCompletionDTO
import br.ufpr.tads.daily_iu_services.domain.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
@Tag(name = "Usuário", description = "Endpoints para gerenciar autenticação de usuário e recuperação de senha")
class UserController(private val userService: UserService) {

    @PostMapping
    @Operation(summary = "Criar Usuário", description = "Criar uma nova conta de usuário")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    fun createUser(@RequestBody @Valid request: UserCreatorDTO, @RequestHeader(value = "x-user-id", required = false) userId: Long?): ResponseEntity<UserDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request, userId))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Usuário", description = "Atualizar uma conta de usuário existente")
    fun updateUser(@PathVariable("id") id: Long, @RequestBody @Valid request: UserEditorDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.updateUser(id, request))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Usuário por ID", description = "Recupera os detalhes do usuário pelo ID do usuário")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<UserSimpleDTO> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login do Usuário",
        description = "Autentica um usuário e retorna um token JWT com os detalhes do usuário"
    )
    fun login(@RequestBody @Valid request: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        return ResponseEntity.ok(userService.login(request))
    }

    @PostMapping("/password/forgot")
    @Operation(
        summary = "Iniciar redefinição de senha",
        description = "Envia um OTP para o e-mail do usuário para redefinição de senha"
    )
    @ApiResponse(responseCode = "204", description = "OTP enviado com sucesso")
    fun resetPassword(@RequestHeader("x-user-email") @Email email: String): ResponseEntity<Void> {
        userService.sendEmailOTP(email)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/password/reset")
    @Operation(
        summary = "Alterar Senha",
        description = "Altera a senha do usuário utilizando o OTP enviado para o e-mail"
    )
    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso")
    fun changePassword(@RequestBody @Valid request: ChangePasswordDTO): ResponseEntity<Void> {
        userService.resetPassword(request)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/workout/plan")
    @Operation(summary = "Obter Plano de Treino", description = "Recuperar o plano de treino de um usuário")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Plano de treino recuperado com sucesso"),
        ApiResponse(responseCode = "204", description = "Nenhum plano de treino ativo encontrado para o usuário")
    )
    fun getWorkoutPlan(@RequestHeader("x-user-id") userId: Long): ResponseEntity<UserWorkoutPlanDTO> {
        return ResponseEntity.ok(userService.getWorkoutPlan(userId))
    }

    @PostMapping("/workout/completion")
    @Operation(summary = "Registrar Conclusão de Treino", description = "Registrar a conclusão de um treino")
    fun logWorkoutCompletion(
        @RequestHeader("x-user-id") userId: Long,
        @RequestBody request: List<WorkoutCompletionDTO>
    ): ResponseEntity<UserWorkoutPlanSimpleDTO> {
        return ResponseEntity.ok(userService.logWorkoutCompletion(userId, request))
    }

    @PostMapping("/workout/feedback")
    @Operation(summary = "Registra Feedback de Treino", description = "Cria uma nova entrada de feedback de treino")
    @ApiResponse(responseCode = "204", description = "Feedback de treino registrado com sucesso")
    fun createWorkoutFeedback(
        @RequestHeader("x-user-id") userId: Long,
        @RequestBody @Valid request: List<ExerciseFeedbackCreatorDTO>
    ): ResponseEntity<Void> {
        userService.createWorkoutFeedback(userId, request)
        return ResponseEntity.noContent().build()
    }
}