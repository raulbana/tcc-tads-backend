package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginResponseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper.UserMapper
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val passwordService: PasswordService,
    private val tokenService: TokenJWTService,
    private val userRepository: UserRepository
) {

    fun createUser() {
        TODO("Not yet implemented")
    }

    fun login(requestDTO: LoginRequestDTO): LoginResponseDTO {
        val user = userRepository.findByEmail(requestDTO.email)
            ?: throw NotFoundException("Usuário ou senha inválidos")

        if (!passwordService.verifyPassword(requestDTO.password, user.credential)) {
            throw NotFoundException("Usuário ou senha inválidos")
        }

        return LoginResponseDTO(
            tokenService.generateToken(user),
            UserMapper.INSTANCE.userToUserDTO(user)
        )
    }

    fun resetPassword() {
        TODO("Not yet implemented")
    }
}