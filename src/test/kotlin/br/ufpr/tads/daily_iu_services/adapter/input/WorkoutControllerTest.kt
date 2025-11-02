package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutDTO
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseCategoryRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.WorkoutRepository
import br.ufpr.tads.daily_iu_services.domain.entity.content.Category
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Exercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseCategory
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Workout
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutExercise
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
class WorkoutControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: com.fasterxml.jackson.databind.ObjectMapper = com.fasterxml.jackson.databind.ObjectMapper()
        .registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
        .registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    @Autowired
    private lateinit var repository: WorkoutRepository

    @Autowired
    private lateinit var exerciseRepository: ExerciseRepository

    @Autowired
    private lateinit var categoryRepository: ExerciseCategoryRepository

    private lateinit var savedExercise: Exercise
    private lateinit var savedCategory: ExerciseCategory

    @BeforeAll
    fun setup() {
        val exCat1 = ExerciseCategory(
            name = "Kegel",
            description = "Exercícios focados na contração e relaxamento do assoalho pélvico, fundamentais para o fortalecimento muscular e controle urinário. Podem ser feitos em qualquer posição e adaptados para diferentes níveis de habilidade."
        )

        savedCategory = categoryRepository.save(exCat1)

        val exercise = Exercise(
            title = "Exercício de Kegel Básico",
            category = savedCategory,
            instructions = "Contraia os músculos do assoalho pélvico, segure por 5 segundos e relaxe por 5 segundos. Repita 10 vezes.",
            repetitions = 10,
            sets = 1,
            restTime = 5,
            duration = 100
        )

        savedExercise = exerciseRepository.save(exercise)

        val workout = Workout(
            name = "Treino de Kegel para Iniciantes",
            description = "Um treino simples focado no fortalecimento do assoalho pélvico através de exercícios de Kegel.",
            totalDuration = 100,
            difficultyLevel = "Iniciante"
        )

        workout.exercises.add(WorkoutExercise(workout = workout, exercise = savedExercise, exerciseOrder = 1))

        repository.save(workout)
    }

    @AfterAll
    fun teardown() {
        repository.deleteAll()
        exerciseRepository.deleteAll()
        categoryRepository.deleteAll()
    }

    @Test
    fun `Deve listar todos os treinos`() {
        val request = MockMvcRequestBuilders
            .get("/v1/exercise/workout")
            .accept(APPLICATION_JSON)

        val result = mvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val responseContent = result.response.contentAsString
        val workouts: List<WorkoutDTO> = mapper.readValue(responseContent, mapper.typeFactory.constructCollectionType(List::class.java, WorkoutDTO::class.java))

        Assertions.assertTrue(workouts.isNotEmpty())
        Assertions.assertTrue(workouts.any { it.name == "Treino de Kegel para Iniciantes" })
    }

    @Test
    @Transactional
    fun `Deve criar um novo treino`() {
        val newWorkout = WorkoutCreatorDTO(
            name = "Treino Avançado de Kegel",
            description = "Um treino avançado focado no fortalecimento do assoalho pélvico através de exercícios de Kegel.",
            totalDuration = 200,
            difficultyLevel = "Avançado",
            exerciseIds = mapOf(1 to savedExercise.id!!)
        )

        val request = MockMvcRequestBuilders
            .post("/v1/exercise/workout")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(newWorkout))
            .accept(APPLICATION_JSON)

        val result = mvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn().response.contentAsString

        val createdWorkout: WorkoutDTO = mapper.readValue(result, WorkoutDTO::class.java)
        Assertions.assertEquals("Treino Avançado de Kegel", createdWorkout.name)
        Assertions.assertTrue(createdWorkout.exercises.any{ it.value.title == "Exercício de Kegel Básico" })
    }

    @Test
    @Transactional
    fun `Deve atualizar um treino existente`() {
        val exNew = Exercise(
            title = "Exercício de Kegel Intermediário",
            category = categoryRepository.findById(savedCategory.id!!).get(),
            instructions = "Contraia os músculos do assoalho pélvico, segure por 10 segundos e relaxe por 10 segundos. Repita 15 vezes.",
            repetitions = 15,
            sets = 1,
            restTime = 5,
            duration = 150
        )
        val savedEx = exerciseRepository.save(exNew)

        val updatedWorkout = WorkoutCreatorDTO(
            name = "Treino de Kegel para Iniciantes",
            description = "Um treino simples focado no fortalecimento do assoalho pélvico através de exercícios de Kegel. Versão atualizada.",
            totalDuration = 120,
            difficultyLevel = "Iniciante",
            exerciseIds = mapOf(1 to savedExercise.id!!, 2 to savedEx.id!!)
        )

        val request = MockMvcRequestBuilders
            .put("/v1/exercise/workout/1")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(updatedWorkout))
            .accept(APPLICATION_JSON)

        val result = mvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val updatedWorkoutResponse: WorkoutDTO = mapper.readValue(result, WorkoutDTO::class.java)
        Assertions.assertEquals(120, updatedWorkoutResponse.totalDuration)
        Assertions.assertTrue(updatedWorkoutResponse.exercises.any{ it.value.title == "Exercício de Kegel Intermediário" })
    }

    @Test
    @Transactional
    fun `Deve deletar um treino existente`() {
        val treinoParaDeletar = Workout(
            name = "Treino para Deletar",
            description = "Treino criado para teste de deleção.",
            totalDuration = 50,
            difficultyLevel = "Iniciante"
        )

        val savedWorkout = repository.save(treinoParaDeletar)
        val request = MockMvcRequestBuilders
            .delete("/v1/exercise/workout/${savedWorkout.id}")
            .accept(APPLICATION_JSON)

        mvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        val exists = repository.existsById(savedWorkout.id!!)
        Assertions.assertFalse(exists)
    }
}