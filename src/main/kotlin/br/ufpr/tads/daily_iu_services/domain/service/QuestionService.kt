package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.OnboardCompleteDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.OnboardSubmitDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.QuestionDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.mapper.QuestionMapper
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper.UserMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.UserWorkoutPlanRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutPlanRepository
import br.ufpr.tads.daily_iu_services.adapter.output.questions.QuestionsRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.PatientProfileRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.UserWorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class QuestionService(
    private val repository: QuestionsRepository,
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val workoutPlanService: WorkoutPlanService,
    private val userWorkoutPlanRepository: UserWorkoutPlanRepository,
    private val patientProfileRepository: PatientProfileRepository
) {

    fun getInitialQuestions(userId: Long?): List<QuestionDTO> {
        val questions = repository.findAll().map { QuestionMapper.INSTANCE.questionToQuestionDTO(it) }

        if (userId != null) {
            val user: User = userRepository.findById(userId).orElseThrow {
                NotFoundException("Usuário com id $userId não encontrado")
            }
            if (user.profile != null) {
                val profile = user.profile!!
                questions.forEach { question ->
                    when (question.id) {
                        "birthdate" -> {
                            question.answer = profile.birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            question.hidden = true
                        }
                        "gender" -> {
                            question.answer = profile.gender
                            question.hidden = true
                        }
                        "q3_frequency" -> question.answer = profile.iciq3answer
                        "q4_amount" -> question.answer = profile.iciq4answer
                        "q5_interference" -> question.answer = profile.iciq5answer
                        "q6_when" -> question.answer = profile.urinationLoss.split(",").toList()
                    }
                }
            }
        }

        return questions
    }

    fun submitOnboardingAnswers(request: OnboardSubmitDTO): OnboardCompleteDTO {
        val birthDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val user: User? = request.userId?.let {
            userRepository.findById(it).orElseThrow {
                NotFoundException("Usuário com id ${request.userId} não encontrado")
            }
        }

        if (user == null || !userService.hasActiveWorkoutPlan(user)) {
            val birthdate = request.answers["birthdate"]
            val q3Frequency = request.answers["q3_frequency"]
            val q4Amount = request.answers["q4_amount"]
            val q5Interference = request.answers["q5_interference"]

            if (birthdate == null || q3Frequency == null || q4Amount == null || q5Interference == null) {
                throw IllegalArgumentException("Perguntas obrigatórias ausentes")
            }

            val birthDate = LocalDate.parse(birthdate, birthDateFormat)
            val age = calculateAge(birthDate)
            val q3Int = q3Frequency.toIntOrNull() ?: 0
            val q4Int = q4Amount.toIntOrNull() ?: 0
            val q5Int = q5Interference.toIntOrNull() ?: 0

            val iciqScore = q3Int + q4Int + q5Int

            val profile: PatientProfile = if (user != null && user.profile != null) {
                user.profile!!.iciq3answer = q3Int
                user.profile!!.iciq4answer = q4Int
                user.profile!!.iciq5answer = q5Int
                user.profile!!.iciqScore = iciqScore
                user.profile!!.urinationLoss = request.answers["q6_when"] ?: ""

                user.profile!!
            } else {
                PatientProfile(
                    birthDate = birthDate,
                    gender = request.answers["gender"] ?: "N",
                    iciq3answer = q3Int,
                    iciq4answer = q4Int,
                    iciq5answer = q5Int,
                    iciqScore = iciqScore,
                    urinationLoss = request.answers["q6_when"] ?: ""
                )
            }
            patientProfileRepository.save(profile)

            val recommendedWorkoutPlans = workoutPlanService.findSuitableWorkoutPlans(age, iciqScore)
            val bestPlan: UserWorkoutPlanDTO? = filterBestSuitablePlan(recommendedWorkoutPlans, age, iciqScore)
                ?.let {
                    val uwp = UserWorkoutPlan(
                        plan = it,
                        user = user,
                        startDate = LocalDateTime.now(),
                    )

                    if (user != null) {
                        userWorkoutPlanRepository.save(uwp)
                    }

                    ExerciseMapper.INSTANCE.userWorkoutPlanEntityToDTO(uwp)
                }

            return OnboardCompleteDTO(
                profile = UserMapper.INSTANCE.patientProfileToPatientProfileDTO(profile)!!,
                workoutPlan = bestPlan
            )
        }
        throw IllegalStateException("Usuário já possui um plano de treino ativo, por favor complete o plano atual antes de realizar a avaliação novamente.")
    }

    private fun filterBestSuitablePlan(plans: List<WorkoutPlan>, age: Int, iciqScore: Int): WorkoutPlan? {
        return plans.minByOrNull { plan ->
            val ageMidpoint = (plan.ageMin + plan.ageMax) / 2
            val iciqMidpoint = (plan.iciqScoreMin + plan.iciqScoreMax) / 2
            val ageDifference = age - ageMidpoint
            val iciqDifference = iciqScore - iciqMidpoint
            ageDifference * ageDifference + iciqDifference * iciqDifference
        }
    }

    private fun calculateAge(input: LocalDate): Int {
        val currentDate = LocalDate.now()
        var age = currentDate.year - input.year
        if (currentDate.dayOfYear < input.dayOfYear) {
            age--
        }
        return age
    }
}