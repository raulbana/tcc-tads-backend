package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttribute
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeType
import br.ufpr.tads.daily_iu_services.adapter.output.exercise.ExerciseAttributeRepository
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
class ExerciseAttributeControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: com.fasterxml.jackson.databind.ObjectMapper = com.fasterxml.jackson.databind.ObjectMapper()
        .registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())

    @Autowired
    private lateinit var repository: ExerciseAttributeRepository

    @BeforeAll
    fun setup() {
        val exAttr1 = ExerciseAttribute(
            name = "Benefício Cardiovascular",
            description = "Melhora a saúde do coração e dos vasos sanguíneos.",
            type = ExerciseAttributeType.BENEFIT.label
        )

        val exAttr2 = ExerciseAttribute(
            name = "Contraindicação para Hipertensão",
            description = "Evitar exercícios intensos para pessoas com pressão alta.",
            type = ExerciseAttributeType.CONTRAINDICATION.label
        )

        val exAttr3 = ExerciseAttribute(
            name = "Benefício para Flexibilidade",
            description = "Aumenta a amplitude de movimento das articulações.",
            type = ExerciseAttributeType.BENEFIT.label
        )

        val exAttr4 = ExerciseAttribute(
            name = "Contraindicação para Lesões Musculares",
            description = "Evitar exercícios que possam agravar lesões musculares existentes.",
            type = ExerciseAttributeType.CONTRAINDICATION.label
        )

        // Save these entities to the test database
        repository.saveAll(listOf(exAttr1, exAttr2, exAttr3, exAttr4))
    }

    @AfterAll
    fun teardown() {
        repository.deleteAll()
    }

    @Test
    fun `Deve listar todos os atributos de exercicios`() {
        val response = mvc.perform(MockMvcRequestBuilders.get("/v1/exercise/attribute")
            .accept("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val attributes: Array<ExerciseAttribute> = mapper.readValue(response, Array<ExerciseAttribute>::class.java)
        Assertions.assertTrue(attributes.size >= 4, "Deveria retornar pelo menos 4 atributos de exercício")
        Assertions.assertTrue(attributes.any { it.name == "Benefício para Flexibilidade" }, "Deveria conter o atributo 'Benefício Cardiovascular'")
    }

    @Test
    fun `Deve criar um novo atributo de exercicio`() {
        val newAttribute = mapOf(
            "name" to "Benefício para Saúde Mental",
            "description" to "Reduz o estresse e melhora o humor.",
            "type" to 1
        )

        val response = mvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/attribute")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newAttribute))
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn().response.contentAsString

        val createdAttribute: ExerciseAttribute = mapper.readValue(response, ExerciseAttribute::class.java)
        Assertions.assertNotNull(createdAttribute.id, "O ID do atributo criado não deve ser nulo")
        Assertions.assertEquals("Benefício para Saúde Mental", createdAttribute.name, "O nome do atributo criado está incorreto")
    }

    @Test
    fun `Deve atualizar um atributo de exercicio existente`() {
        val existingAttribute = repository.findAll().first()
        val updatedData = mapOf(
            "name" to "Benefício Cardiovascular Atualizado",
            "description" to "Melhora significativamente a saúde do coração.",
            "type" to 1
        )

        val response = mvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/attribute/${existingAttribute.id}")
                .contentType("application/json")
                .content(mapper.writeValueAsString(updatedData))
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val updatedAttribute: ExerciseAttribute = mapper.readValue(response, ExerciseAttribute::class.java)
        Assertions.assertEquals("Benefício Cardiovascular Atualizado", updatedAttribute.name, "O nome do atributo atualizado está incorreto")
    }

    @Test
    fun `Deve deletar um atributo de exercicio existente`() {
        val exAtrrToDelete = ExerciseAttribute(
            name = "Atributo para Deletar",
            description = "Este atributo será deletado no teste.",
            type = ExerciseAttributeType.BENEFIT.label
        )
        repository.save(exAtrrToDelete)

        val existingAttribute = repository.findAll().last()

        mvc.perform(
            MockMvcRequestBuilders.delete("/v1/exercise/attribute/${existingAttribute.id}")
                .accept("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        val deletedAttribute = repository.findById(existingAttribute.id!!)
        Assertions.assertTrue(deletedAttribute.isEmpty, "O atributo deveria ter sido deletado")
    }
}
