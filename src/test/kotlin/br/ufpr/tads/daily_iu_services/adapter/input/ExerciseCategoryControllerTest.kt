package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseCategoryRepository
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseCategory
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseCategoryControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: com.fasterxml.jackson.databind.ObjectMapper = com.fasterxml.jackson.databind.ObjectMapper()
        .registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())

    @Autowired
    private lateinit var repository: ExerciseCategoryRepository

    @BeforeAll
    fun setup() {
        val exCat1 = ExerciseCategory(
            name = "Cardio",
            description = "Exercícios que aumentam a frequência cardíaca e melhoram a resistência cardiovascular."
        )

        val exCat2 = ExerciseCategory(
            name = "Força",
            description = "Exercícios focados no desenvolvimento da força muscular e tonificação."
        )

        // Save these entities to the test database
        repository.saveAll(listOf(exCat1, exCat2))
    }

    @AfterAll
    fun teardown() {
        repository.deleteAll()
    }

    @Test
    fun `Deve listar todas as categorias de exercicio`() {
        val result = mvc.perform(
            MockMvcRequestBuilders.get("/v1/exercise/category")
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val responseBody = result.response.contentAsString
        val categories: List<ExerciseCategory> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, ExerciseCategory::class.java)
        )

        Assertions.assertTrue(categories.size >= 2)
    }

    @Test
    fun `Deve criar uma nova categoria de exercicio`() {
        val newCategory = ExerciseCategory(
            name = "Flexibilidade",
            description = "Exercícios que melhoram a amplitude de movimento das articulações."
        )

        val requestBody = mapper.writeValueAsString(newCategory)

        val result = mvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/category")
                .contentType("application/json")
                .content(requestBody)
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        val responseBody = result.response.contentAsString
        val createdCategory: ExerciseCategory = mapper.readValue(responseBody, ExerciseCategory::class.java)

        Assertions.assertEquals(newCategory.name, createdCategory.name)
        Assertions.assertEquals(newCategory.description, createdCategory.description)
    }

    @Test
    fun `Deve atualizar uma categoria de exercicio existente`() {
        val existingCategory = repository.findAll().first()
        val updatedCategory = ExerciseCategory(
            id = existingCategory.id,
            name = "Cardio Atualizado",
            description = "Descrição atualizada para exercícios cardiovasculares."
        )

        val requestBody = mapper.writeValueAsString(updatedCategory)

        val result = mvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/category/${existingCategory.id}")
                .contentType("application/json")
                .content(requestBody)
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val responseBody = result.response.contentAsString
        val categoryResponse: ExerciseCategory = mapper.readValue(responseBody, ExerciseCategory::class.java)

        Assertions.assertEquals(updatedCategory.name, categoryResponse.name)
        Assertions.assertEquals(updatedCategory.description, categoryResponse.description)
    }

    @Test
    fun `Deve excluir uma categoria de exercicio existente`() {
        val existingCategory = repository.findAll().first()

        mvc.perform(
            MockMvcRequestBuilders.delete("/v1/exercise/category/${existingCategory.id}")
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        val deletedCategory = repository.findById(existingCategory.id!!)
        Assertions.assertTrue(deletedCategory.isEmpty)
    }
}