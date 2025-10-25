package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutPlanRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Workout
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlanWorkout
import jakarta.transaction.Transactional
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutPlanControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: com.fasterxml.jackson.databind.ObjectMapper = com.fasterxml.jackson.databind.ObjectMapper()
        .registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
        .registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    @Autowired
    private lateinit var repository: WorkoutPlanRepository

    @Autowired
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var savedWorkout: Workout
    private lateinit var savedWorkoutPlan: WorkoutPlan

    @BeforeAll
    fun setup() {
        val workout = Workout(
            name = "Treino de Kegel para Iniciantes",
            description = "Um treino simples focado no fortalecimento do assoalho pélvico através de exercícios de Kegel.",
            totalDuration = 100,
            difficultyLevel = "Iniciante"
        )

        savedWorkout = workoutRepository.save(workout)

        val workoutPlan = WorkoutPlan(
            name = "Plano de Treino Semanal",
            description = "Um plano de treino semanal focado no fortalecimento do assoalho pélvico.",
            daysPerWeek = 3,
            totalWeeks = 4,
            iciqScoreMin = 4,
            iciqScoreMax = 8,
            ageMin = 16,
            ageMax = 65
        )

        workoutPlan.workouts.add(
            WorkoutPlanWorkout(
                workout = savedWorkout,
                workoutPlan = workoutPlan,
                workoutOrder = 1
            )
        )
        savedWorkoutPlan = repository.save(workoutPlan)
    }

    @AfterAll
    fun teardown() {
        repository.delete(savedWorkoutPlan)
        workoutRepository.delete(savedWorkout)
    }

    @Test
    fun `Deve listar todos os planos de treino`() {
        val result = mvc.perform(
            MockMvcRequestBuilders.get("/v1/exercise/workout/plan")
                .accept(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val responseBody = result.response.contentAsString
        val workoutPlans: List<WorkoutPlanDTO> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, WorkoutPlanDTO::class.java)
        )

        Assertions.assertTrue(workoutPlans.isNotEmpty())
        Assertions.assertTrue(workoutPlans.any { it.name == "Plano de Treino Semanal" })
    }

    @Test
    @Transactional
    fun `Deve criar um novo plano de treino`() {
        val newWorkoutPlan = WorkoutPlanCreatorDTO(
            name = "Plano de Treino Diário",
            description = "Um plano de treino diário focado no fortalecimento do assoalho pélvico.",
            daysPerWeek = 7,
            totalWeeks = 2,
            iciqScoreMin = 3,
            iciqScoreMax = 10,
            ageMin = 18,
            ageMax = 65,
            workoutIds = mapOf(1 to savedWorkout.id!!)
        )

        val result = mvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/workout/plan")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newWorkoutPlan))
                .accept(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        val responseBody = result.response.contentAsString
        val createdWorkoutPlan: WorkoutPlanDTO = mapper.readValue(responseBody, WorkoutPlanDTO::class.java)

        Assertions.assertEquals("Plano de Treino Diário", createdWorkoutPlan.name)
        Assertions.assertEquals(7, createdWorkoutPlan.daysPerWeek)
    }

    @Test
    @Transactional
    fun `Deve atualizar um plano de treino existente`() {
        val newWorkout = Workout(
            name = "Treino de Kegel Intermediário",
            description = "Um treino intermediário focado no fortalecimento do assoalho pélvico através de exercícios de Kegel.",
            totalDuration = 150,
            difficultyLevel = "Intermediário"
        )

        val savedWorkout = workoutRepository.save(newWorkout)

        val updatedWorkoutPlan = WorkoutPlanCreatorDTO(
            name = "Plano de Treino Semanal",
            description = "Um plano de treino semanal atualizado focado no fortalecimento do assoalho pélvico.",
            daysPerWeek = 4,
            totalWeeks = 6,
            iciqScoreMin = 5,
            iciqScoreMax = 9,
            ageMin = 18,
            ageMax = 60,
            workoutIds = mapOf(1 to savedWorkout.id!!)
        )

        val result = mvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/workout/plan/1")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedWorkoutPlan))
                .accept(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val responseBody = result.response.contentAsString
        val updatedWorkoutPlanResponse: WorkoutPlanDTO = mapper.readValue(responseBody, WorkoutPlanDTO::class.java)

        Assertions.assertEquals(4, updatedWorkoutPlanResponse.daysPerWeek)
        Assertions.assertTrue(updatedWorkoutPlanResponse.workouts.any { it.value.name == "Treino de Kegel Intermediário" })
        Assertions.assertFalse(updatedWorkoutPlanResponse.workouts.any { it.value.name == "Treino de Kegel para Iniciantes" })
    }

    @Test
    @Transactional
    fun `Deve deletar um plano de treino existente`() {
        val planToDelete = WorkoutPlan(
            name = "Plano de Treino Mensal",
            description = "Um plano de treino mensal focado no fortalecimento do assoalho pélvico.",
            daysPerWeek = 2,
            totalWeeks = 8,
            iciqScoreMin = 6,
            iciqScoreMax = 12,
            ageMin = 20,
            ageMax = 70
        )

        val savedPlan = repository.save(planToDelete)

        mvc.perform(
            MockMvcRequestBuilders.delete("/v1/exercise/workout/plan/${savedPlan.id}")
                .accept(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        val exists = repository.existsById(savedPlan.id!!)
        Assertions.assertFalse(exists)
    }
}