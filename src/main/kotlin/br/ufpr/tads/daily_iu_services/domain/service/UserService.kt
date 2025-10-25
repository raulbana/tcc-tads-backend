package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.ChangePasswordDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.ExerciseFeedbackCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginResponseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.WorkoutCompletionDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper.UserMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseFeedbackRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.UserWorkoutPlanRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.MailClient
import br.ufpr.tads.daily_iu_services.adapter.output.user.OTPRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseFeedback
import br.ufpr.tads.daily_iu_services.domain.entity.user.Credential
import br.ufpr.tads.daily_iu_services.domain.entity.user.OTP
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import br.ufpr.tads.daily_iu_services.exception.NoContentException
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class UserService (
    private val passwordService: PasswordService,
    private val tokenService: TokenJWTService,
    private val calendarService: CalendarService,
    private val userRepository: UserRepository,
    private val otpRepository: OTPRepository,
    private val userWorkoutPlanRepository: UserWorkoutPlanRepository,
    private val exerciseFeedbackRepository: ExerciseFeedbackRepository,
    private val mailClient: MailClient
) {

    fun createUser() {
        TODO("Not yet implemented")
    }

    fun updateUser() {
        TODO("Not yet implemented")
    }

    fun getUserById(id: Long) {
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

    fun sendEmailOTP(email: String) {
        val user = userRepository.findByEmail(email)

        if (user != null) {
            val otp = generateOTP(user)
            mailClient.sendOtpEmail(user.email, otp)
        }
    }

    fun resetPassword(request: ChangePasswordDTO) {
        val user = userRepository.findByEmail(request.email)
            ?: throw NotFoundException("Usuário com e-mail ${request.email} não encontrado")

        val otp = otpRepository.findByUserIdAndUsedFalse(user.id!!)
            ?: throw NotFoundException("Código OTP inválido ou expirado")

        if (otp.expirationTime < System.currentTimeMillis() || otp.code != passwordService.hashPassword(
                request.otp,
                user.credential.salt
            )
        ) {
            throw NotFoundException("Código OTP inválido ou expirado")
        }

        val salt = passwordService.generateSalt()
        val hashedPassword = passwordService.hashPassword(request.newPassword, salt)
        val newCredential = Credential(passwordHash = hashedPassword, salt = salt)

        user.credential = newCredential
        userRepository.save(user)

        // Mark OTP as used
        otp.used = true
        otpRepository.save(otp)
    }

    private fun generateOTP(user: User): OTP {
        val code = SecureRandom().nextInt(900000) + 100000
        val otpCode = passwordService.hashPassword(code.toString(), user.credential.salt)

        val otp = OTP(
            user = user,
            code = otpCode,
            auxCode = code.toString(),
            expirationTime = System.currentTimeMillis() + 5 * 60 * 1000,
            used = false
        )

        return otpRepository.save(otp)
    }

    fun getWorkoutPlan(userId: Long): UserWorkoutPlanDTO {
        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("Usuário com ID $userId não encontrado")
        }

        val userWorkoutPlan = userWorkoutPlanRepository.findByUserAndCompletedFalse(user)
            ?: throw NoContentException("Usuário com ID $userId não possui um plano de treino ativo")

        return ExerciseMapper.INSTANCE.userWorkoutPlanEntityToDTO(userWorkoutPlan)
    }

    fun logWorkoutCompletion(userId: Long, request: List<WorkoutCompletionDTO>) {
        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("Usuário com ID $userId não encontrado")
        }

        val userWorkoutPlan = userWorkoutPlanRepository.findByUserAndCompletedFalse(user)
            ?: throw NotFoundException("Usuário com ID $userId não possui um plano de treino ativo")

        for (completion in request) {
            val workoutPlanWorkout = userWorkoutPlan.plan.workouts.find { it.workout.id == completion.workoutId }
                ?: throw NotFoundException("Treino com ID ${completion.workoutId} não encontrado no plano de treino do usuário")

            // Lógica para registrar a conclusão do treino
            userWorkoutPlan.lastWorkoutDate = completion.completedAt
            userWorkoutPlan.nextWorkout = userWorkoutPlan.nextWorkout?.plus(1) ?: 1
            userWorkoutPlan.weekProgress += 1
            userWorkoutPlan.totalProgress += 1

            // Verifica se a semana foi concluída
            if (userWorkoutPlan.weekProgress >= userWorkoutPlan.plan.daysPerWeek) {
                userWorkoutPlan.currentWeek += 1
                userWorkoutPlan.weekProgress = 0
            }

            // Verifica se o plano foi concluído
            if (userWorkoutPlan.totalProgress >= userWorkoutPlan.plan.daysPerWeek * userWorkoutPlan.plan.totalWeeks) {
                userWorkoutPlan.completed = true
                userWorkoutPlan.nextWorkout = null
                userWorkoutPlan.endDate = completion.completedAt
            }

            // Se o plano não foi concluído, e o próximo treino ultrapassa o total de treinos, reseta para o primeiro treino
            if (!userWorkoutPlan.completed && userWorkoutPlan.nextWorkout!! >= userWorkoutPlan.plan.workouts.size) {
                userWorkoutPlan.nextWorkout = 1
            }

            // Registra o progresso no calendário
            calendarService.registerExerciseCompletion(
                userId,
                completion.completedAt.toLocalDate(),
                workoutPlanWorkout.workout.exercises.size
            )

            userWorkoutPlanRepository.save(userWorkoutPlan)
        }
    }

    fun createWorkoutFeedback(userId: Long, request: List<ExerciseFeedbackCreatorDTO>) {
        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("Usuário com ID $userId não encontrado")
        }

        val userWorkoutPlan = userWorkoutPlanRepository.findByUserAndCompletedFalse(user)
            ?: throw NoContentException("Usuário com ID $userId não possui um plano de treino ativo")

        val feedbacks: MutableList<ExerciseFeedback> = mutableListOf()
        for (feedbackDTO in request) {
            val workout = userWorkoutPlan.plan.workouts.find { it.workout.id == feedbackDTO.workoutId }
                ?: throw NotFoundException("Treino com ID ${feedbackDTO.workoutId} não encontrado no plano de treino do usuário")

            val exercise = workout.workout.exercises.find { it.exercise.id == feedbackDTO.exerciseId }
                ?: throw NotFoundException("Exercício com ID ${feedbackDTO.exerciseId} não encontrado no treino do plano de treino do usuário")

            val feedback = ExerciseFeedback(
                user = user,
                exercise = exercise.exercise,
                workout = workout.workout,
                evaluation = feedbackDTO.evaluation,
                rating = feedbackDTO.rating,
                comments = feedbackDTO.comments ?: "",
                completedAt = feedbackDTO.completedAt ?: LocalDate.now()
            )

            feedbacks.add(feedback)
        }

        exerciseFeedbackRepository.saveAll(feedbacks)
    }
}