package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper.ExerciseMapper
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.OnboardCompleteDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.OnboardSubmitDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.QuestionDTO
import br.ufpr.tads.daily_iu_services.adapter.input.questions.dto.mapper.QuestionMapper
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.mapper.UserMapper
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutPlanRepository
import br.ufpr.tads.daily_iu_services.adapter.output.questions.QuestionsRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.PatientProfileRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.user.PatientProfile
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class QuestionService(
    private val repository: QuestionsRepository,
    private val workoutPlanService: WorkoutPlanService,
    private val patientProfileRepository: PatientProfileRepository
) {

    fun getInitialQuestions(): List<QuestionDTO> {
        return repository.findAll().map { QuestionMapper.INSTANCE.questionToQuestionDTO(it) }
    }

    fun submitOnboardingAnswers(request: OnboardSubmitDTO): OnboardCompleteDTO {
        val birthDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val birthdate = request.answers["birthdate"]
        val q3Frequency = request.answers["q3_frequency"]
        val q4Amount = request.answers["q4_amount"]
        val q5Interference = request.answers["q5_interference"]

        if (birthdate == null || q3Frequency == null || q4Amount == null || q5Interference == null) {
            throw IllegalArgumentException("Missing required answers")
        }

        val birthDate = LocalDate.parse(birthdate, birthDateFormat)
        val age = calculateAge(birthDate)
        val q3Int = q3Frequency.toIntOrNull() ?: 0
        val q4Int = q4Amount.toIntOrNull() ?: 0
        val q5Int = q5Interference.toIntOrNull() ?: 0

        val iciqScore = q3Int + q4Int + q5Int

        val profile = PatientProfile(
            birthDate = birthDate,
            gender = request.answers["gender"] ?: "N",
            iciq3answer = q3Int,
            iciq4answer = q4Int,
            iciq5answer = q5Int,
            iciqScore = iciqScore,
            urinationLoss = request.answers["q6_when"] ?: ""
        )

        patientProfileRepository.save(profile)

        val recommendedWorkoutPlans = workoutPlanService.findSuitableWorkoutPlans(age, iciqScore)
        val bestPlan: UserWorkoutPlanDTO? = filterBestSuitablePlan(recommendedWorkoutPlans, age, iciqScore)
            ?.let {
                UserWorkoutPlanDTO(
                    id = null,
                    plan = ExerciseMapper.INSTANCE.workoutPlanEntityToDTO(it),
                    startDate = LocalDateTime.now(),
                    endDate = null,
                    totalProgress = 0,
                    weekProgress = 0,
                    currentWeek = 1,
                    nextWorkout = 1,
                    lastWorkoutDate = null,
                    completed = false
                )
            }

        return OnboardCompleteDTO(
            profile = UserMapper.INSTANCE.patientProfileToPatientProfileDTO(profile),
            workoutPlan = bestPlan
        )
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