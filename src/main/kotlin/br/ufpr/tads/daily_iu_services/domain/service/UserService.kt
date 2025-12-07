package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.ChangePasswordDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.ExerciseFeedbackCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.LoginResponseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.RoleCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserEditorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.WorkoutCompletionDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper.UserMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseFeedbackRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.UserWorkoutPlanRepository
import br.ufpr.tads.daily_iu_services.adapter.output.media.MediaRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.MailClient
import br.ufpr.tads.daily_iu_services.adapter.output.user.OTPRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseFeedback
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.UserWorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.user.Credential
import br.ufpr.tads.daily_iu_services.domain.entity.user.OTP
import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import br.ufpr.tads.daily_iu_services.domain.entity.user.Preferences
import br.ufpr.tads.daily_iu_services.domain.entity.user.Role
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import br.ufpr.tads.daily_iu_services.exception.NoContentException
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.security.SecureRandom
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class UserService(
    private val passwordService: PasswordService,
    private val tokenService: TokenJWTService,
    private val calendarService: CalendarService,
    private val workoutPlanService: WorkoutPlanService,
    private val contentService: ContentService,
    private val userRepository: UserRepository,
    private val otpRepository: OTPRepository,
    private val mediaRepository: MediaRepository,
    private val userWorkoutPlanRepository: UserWorkoutPlanRepository,
    private val exerciseFeedbackRepository: ExerciseFeedbackRepository,
    private val mailClient: MailClient
) {

    fun createUser(request: UserCreatorDTO, userId: Long?): UserDTO {
        val creator = userId?.let {
            userRepository.findById(it).orElseThrow {
                NotFoundException("Usuário criador com ID $it não encontrado")
            }
        }

        val role = if (creator != null && (creator.role.permissionLevel < 3)) {
            // Se o usuário criador não for admin (permissão nível 3), não pode criar usuários
            throw IllegalArgumentException("Usuário com ID ${creator.id} não tem permissão para criar outros usuários.")
        } else if (creator == null) {
            // Se o usuário criador for nulo, o usuário está se auto-registrando, então só pode criar usuários com permissão nível 1
            geraRoleDefault()
        } else {
            // Se o criador for admin, mas não forneceu uma role, atribui a role padrão (nível 1)
            if (request.role == null) geraRoleDefault() else geraRoleFromDTO(request.role, creator)
        }

        val salt = passwordService.generateSalt()
        val credential = Credential(
            passwordHash = passwordService.hashPassword(request.password, salt),
            salt = salt
        )

        val patientProfile = request.profile?.let {
            PatientProfile(
                birthDate = it.birthDate,
                gender = it.gender,
                iciq3answer = it.iciq3answer,
                iciq4answer = it.iciq4answer,
                iciq5answer = it.iciq5answer,
                iciqScore = it.iciqScore,
                urinationLoss = it.urinationLoss
            )
        }

        val preferences = UserMapper.INSTANCE.preferencesDTOToPreferences(request.preferences) ?: Preferences(
            highContrast = false,
            bigFont = false,
            darkMode = false,
            reminderWorkout = false,
            reminderCalendar = false,
            encouragingMessages = false
        )

        val profilePicture = UserMapper.INSTANCE.mediaDTOToEntity(request.profilePicture)
            ?.let { if (it.id == null) mediaRepository.save(it) else it }

        val newUser = User(
            name = request.name,
            email = request.email,
            profilePicture = profilePicture,
            profile = patientProfile,
            credential = credential,
            preferences = preferences,
            role = role
        )

        val savedUser = userRepository.save(newUser)

        if (request.workoutPlan != null) {
            assignWorkoutPlanToUser(savedUser, request.workoutPlan)
        }

        return UserMapper.INSTANCE.userToUserDTO(savedUser)
    }

    private fun geraRoleDefault(): Role {
        return Role(
            description = "Usuário comum",
            permissionLevel = 1,
            reason = "Acesso padrão ao aplicativo.",
            conceivedBy = null,
            conceivedAt = LocalDateTime.now()
        )
    }

    private fun geraRoleFromDTO(request: RoleCreatorDTO, creator: User): Role {
        return Role(
            description = request.description,
            permissionLevel = request.permissionLevel,
            reason = request.reason,
            hasDocument = true,
            documentType = request.documentType,
            documentValue = request.documentValue,
            conceivedBy = creator,
            conceivedAt = LocalDateTime.now()
        )
    }

    private fun assignWorkoutPlanToUser(user: User, assignment: UserWorkoutPlanCreatorDTO) {
        val workoutPlan = workoutPlanService.getWorkoutPlanById(assignment.planId)
        if (workoutPlan != null) {
            val userWorkoutPlan = UserWorkoutPlan(
                plan = workoutPlan,
                user = user,
                startDate = assignment.startDate,
                endDate = assignment.endDate,
                totalProgress = assignment.totalProgress,
                weekProgress = assignment.weekProgress,
                currentWeek = assignment.currentWeek,
                completed = assignment.completed
            )
            userWorkoutPlanRepository.save(userWorkoutPlan)
        }
    }

    fun updateUser(id: Long, request: UserEditorDTO): UserDTO {
        val user = userRepository.findById(id).orElseThrow {
            NotFoundException("Usuário com ID $id não encontrado")
        }

        val profilePicture = UserMapper.INSTANCE.mediaDTOToEntity(request.profilePicture)
            ?.let { if (it.id == null) mediaRepository.save(it) else it }

        if (request.name != null) user.name = request.name

        if (request.email != null) user.email = request.email

        if (request.profilePicture != null) user.profilePicture = profilePicture

        val updatedUser = userRepository.save(user)
        return UserMapper.INSTANCE.userToUserDTO(updatedUser)
    }

    fun getUserById(id: Long): UserSimpleDTO {
        val user = userRepository.findById(id).orElseThrow {
            NotFoundException("Usuário com ID $id não encontrado")
        }

        val contentStats = contentService.getUserContentStats(user.id!!)
        val curtidas = contentStats["curtidas"] ?: 0
        val salvos = contentStats["salvos"] ?: 0
        val postagens = contentStats["postagens"] ?: 0

        return UserMapper.INSTANCE.userToUserSimpleDTO(user, curtidas, salvos, postagens)
    }

    fun login(requestDTO: LoginRequestDTO): LoginResponseDTO {
        val user = userRepository.findByEmailAndBlockedFalse(requestDTO.email)
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
        val user = userRepository.findByEmailAndBlockedFalse(email)

        if (user != null) {
            val otp = generateOTP(user)
            mailClient.sendOtpEmail(user.email, otp)
        }
    }

    fun resetPassword(request: ChangePasswordDTO) {
        val user = userRepository.findByEmailAndBlockedFalse(request.email)
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

    fun hasActiveWorkoutPlan(user: User): Boolean {
        val userWorkoutPlan = userWorkoutPlanRepository.findByUserAndCompletedFalse(user)

        return userWorkoutPlan != null
    }

    fun logWorkoutCompletion(userId: Long, request: List<WorkoutCompletionDTO>): UserWorkoutPlanSimpleDTO {
        if (request.isEmpty()) {
            throw IllegalArgumentException("A lista de conclusões de treino não pode estar vazia")
        }

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
        }

        val savedUserWorkoutPlan = userWorkoutPlanRepository.save(userWorkoutPlan)
        return ExerciseMapper.INSTANCE.userWorkoutPlanEntityToSimpleDTO(savedUserWorkoutPlan)
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