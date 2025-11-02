package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseAttributeRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseCategoryRepository
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseRepository
import br.ufpr.tads.daily_iu_services.adapter.output.media.MediaRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Exercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttribute
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeExercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeType
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseCategory
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseMedia
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import jakarta.transaction.Transactional
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: com.fasterxml.jackson.databind.ObjectMapper = com.fasterxml.jackson.databind.ObjectMapper()
        .registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
        .registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    @Autowired
    private lateinit var repository: ExerciseRepository

    @Autowired
    private lateinit var categoryRepository: ExerciseCategoryRepository

    @Autowired
    private lateinit var attributeRepository: ExerciseAttributeRepository

    @Autowired
    private lateinit var mediaRepository: MediaRepository

    @BeforeAll
    fun setup() {
        val exCat1 = ExerciseCategory(
            name = "Kegel",
            description = "Exercícios focados na contração e relaxamento do assoalho pélvico, fundamentais para o fortalecimento muscular e controle urinário. Podem ser feitos em qualquer posição e adaptados para diferentes níveis de habilidade."
        )

        val exAttr1 = ExerciseAttribute(
            name = "Benefício para Saúde Pélvica",
            description = "Fortalece os músculos do assoalho pélvico, melhorando o controle urinário e a saúde sexual.",
            type = ExerciseAttributeType.BENEFIT.label
        )

        val media = Media(
            url = "http://127.0.0.1:10000/devstoreaccount1/media/91b2247e-070f-4668-8d1f-11063629811a.jpg",
            contentType = "image/jpeg",
            contentSize = 107015L,
            altText = "Porção de panquecas de banana e aveia com mel e canela em um prato branco sobre uma mesa de madeira clara.",
            createdAt = LocalDateTime.now()
        )

        val exercise = Exercise(
            title = "Exercício de Kegel Básico",
            category = categoryRepository.save(exCat1),
            instructions = "Contraia os músculos do assoalho pélvico, segure por 5 segundos e relaxe por 5 segundos. Repita 10 vezes.",
            repetitions = 10,
            sets = 1,
            restTime = 5,
            duration = 100
        )

        exercise.attributes.add(
            ExerciseAttributeExercise(
                exercise = exercise,
                attribute = attributeRepository.save(exAttr1)
            )
        )
        exercise.media.add(ExerciseMedia(exercise = exercise, media = mediaRepository.save(media)))

        repository.save(exercise)
    }

    @AfterAll
    fun teardown() {
        repository.deleteAll()
        categoryRepository.deleteAll()
        attributeRepository.deleteAll()
        mediaRepository.deleteAll()
    }

    @Test
    fun `Deve listar todos os exercicios`() {
        val response = mvc.perform(
            MockMvcRequestBuilders.get("/v1/exercise")
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val exercises = mapper.readValue(response, Array<ExerciseDTO>::class.java)
        Assertions.assertTrue(exercises.isNotEmpty(), "Deveria retornar pelo menos 1 exercício")
        Assertions.assertTrue(
            exercises.any { it.title == "Exercício de Kegel Básico" },
            "Deveria conter o exercício 'Exercício de Kegel Básico'"
        )
    }

    @Test
    @Transactional
    fun `Deve criar um novo exercicio`() {
        val newExercise = ExerciseCreatorDTO(
            title = "Exercício de Agachamento",
            categoryId = categoryRepository.findAll().first().id!!,
            instructions = "Fique em pé com os pés na largura dos ombros. Dobre os joelhos e abaixe o corpo como se fosse sentar em uma cadeira. Volte à posição inicial. Repita 15 vezes.",
            repetitions = 15,
            sets = 3,
            restTime = 30,
            duration = 200,
            attributes = attributeRepository.findAll().map { it.id!! },
            media = listOf(
                MediaDTO(
                    id = 1L,
                    url = "http://127.0.0.1:10000/devstoreaccount1/media/91b2247e-070f-4668-8d1f-11063629811a.jpg",
                    contentType = "image/jpeg",
                    contentSize = 107015L,
                    altText = "Porção de panquecas de banana e aveia com mel e canela em um prato branco sobre uma mesa de madeira clara.",
                    createdAt = LocalDateTime.now()
                )
            )
        )

        val response = mvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newExercise))
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn().response.contentAsString

        val createdExercise = mapper.readValue(response, ExerciseDTO::class.java)
        Assertions.assertNotNull(createdExercise.id, "O ID do exercício criado não deve ser nulo")
        Assertions.assertEquals(
            "Exercício de Agachamento",
            createdExercise.title,
            "O título do exercício criado está incorreto"
        )
    }

    @Test
    @Transactional
    fun `Deve atualizar um exercicio existente`() {
        val existingExercise = repository.findAll().first()
        val updatedExercise = ExerciseCreatorDTO(
            title = "Exercício de Kegel Básico",
            categoryId = existingExercise.category.id!!,
            instructions = "Contraia os músculos do assoalho pélvico, segure por 10 segundos e relaxe por 5 segundos. Repita 15 vezes.",
            repetitions = 15,
            sets = 2,
            restTime = 10,
            duration = 150,
            attributes = existingExercise.attributes.map { it.attribute.id!! },
            media = existingExercise.media.map {
                MediaDTO(
                    id = it.media.id!!,
                    url = it.media.url,
                    contentType = it.media.contentType,
                    contentSize = it.media.contentSize,
                    altText = it.media.altText,
                    createdAt = it.media.createdAt
                )
            }
        )

        val response = mvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/${existingExercise.id}")
                .contentType("application/json")
                .content(mapper.writeValueAsString(updatedExercise))
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val updatedExerciseResponse = mapper.readValue(response, ExerciseDTO::class.java)
        Assertions.assertEquals(
            15,
            updatedExerciseResponse.repetitions,
            "O número de repetições do exercício atualizado está incorreto"
        )
        Assertions.assertEquals(
            2,
            updatedExerciseResponse.sets,
            "O número de séries do exercício atualizado está incorreto"
        )
    }

    @Test
    @Transactional
    fun `Deve deletar um exercicio existente`() {
        val exParaDeletar = Exercise(
            title = "Exercício de Flexão de Braço",
            category = categoryRepository.findAll().first(),
            instructions = "Deite-se de barriga para baixo com as mãos apoiadas no chão na largura dos ombros. Empurre o corpo para cima até que os braços estejam estendidos e depois abaixe-se novamente. Repita 10 vezes.",
            repetitions = 10,
            sets = 3,
            restTime = 20,
            duration = 150
        )

        val savedExercise = repository.save(exParaDeletar)
        mvc.perform(
            MockMvcRequestBuilders.delete("/v1/exercise/${savedExercise.id}")
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
        val deletedExercise = repository.findById(savedExercise.id!!)
        Assertions.assertTrue(deletedExercise.isEmpty, "O exercício deveria ter sido deletado")
    }
}