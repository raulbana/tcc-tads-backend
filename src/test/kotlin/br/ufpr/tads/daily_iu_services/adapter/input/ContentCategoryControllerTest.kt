package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentCategoryRepository
import br.ufpr.tads.daily_iu_services.domain.entity.content.Category
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContentCategoryControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val mapper: com.fasterxml.jackson.databind.ObjectMapper = com.fasterxml.jackson.databind.ObjectMapper()
        .registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
        .registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    @Autowired
    private lateinit var repository: ContentCategoryRepository

    @BeforeAll
    fun setup() {
        val contentCategory1 = Category(
            name = "Alimentação e Nutrição",
            description = "Dicas de alimentos que ajudam na saúde do trato urinário, receitas funcionais, e orientações nutricionais.",
            auditable = false,
            createdAt = null
        )

        val contentCategory2 = Category(
            name = "Hábitos Saudáveis",
            description = "Rotinas que favorecem o bem-estar, como exercícios físicos, sono, hidratação e autocuidado.",
            auditable = false,
            createdAt = null
        )

        val contentCategory3 = Category(
            name = "Dicas de Fisioterapia Pélvica",
            description = "Conteúdos educativos sobre exercícios, técnicas e orientações fisioterapêuticas.",
            auditable = true,
            createdAt = null
        )

        val contentCategory4 = Category(
            name = "Depoimentos e Histórias Reais",
            description = "Espaço para relatos de superação, experiências com tratamentos e apoio emocional.",
            auditable = false,
            createdAt = null
        )

        val contentCategory5 = Category(
            name = "Mitos e Verdades",
            description = "Desmistificação de crenças populares sobre incontinência urinária e saúde íntima.",
            auditable = true,
            createdAt = null
        )

        val contentCategory6 = Category(
            name = "Profissionais Respondem",
            description = "Sessão para perguntas e respostas com especialistas (fisioterapeutas, nutricionistas, médicos).",
            auditable = true,
            createdAt = null
        )

        // Save these entities to the test database
        repository.saveAll(listOf(contentCategory1, contentCategory2, contentCategory3, contentCategory4, contentCategory5, contentCategory6))
    }

    @AfterAll
    fun teardown() {
        repository.deleteAll()
    }

    @Test
    fun `Deve listar todas as categorias de conteudo`() {
        val requestBuilder = MockMvcRequestBuilders
            .get("/v1/content/category")
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val categories: List<Category> = mapper.readValue(response, Array<Category>::class.java).toList()
        Assertions.assertTrue(categories.size >= 6, "Deveria retornar pelo menos 6 categorias de conteúdo")
    }

    @Test
    fun `Deve criar nova categoria de conteudo`() {
        val newCategory = Category(
            name = "Saúde Mental",
            description = "Conteúdos relacionados ao impacto da saúde mental na incontinência urinária.",
            auditable = false,
            createdAt = null
        )

        val requestBody = mapper.writeValueAsString(newCategory)

        val requestBuilder = MockMvcRequestBuilders
            .post("/v1/content/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn().response.contentAsString

        val createdCategory: Category = mapper.readValue(response, Category::class.java)
        Assertions.assertNotNull(createdCategory.id, "A categoria criada deveria ter um ID atribuído")
        Assertions.assertEquals(newCategory.name, createdCategory.name, "O nome da categoria criada deveria corresponder ao enviado")
    }

    @Test
    fun `Deve atualizar categoria de conteudo existente`() {
        val existingCategory = repository.findAll().first()
        val updatedCategory = existingCategory.copy(
            name = "Nome Atualizado",
            description = "Descrição atualizada da categoria.",
            createdAt = null
        )

        val requestBody = mapper.writeValueAsString(updatedCategory)

        val requestBuilder = MockMvcRequestBuilders
            .put("/v1/content/category/${existingCategory.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val returnedCategory: Category = mapper.readValue(response, Category::class.java)
        Assertions.assertEquals(updatedCategory.name, returnedCategory.name, "O nome da categoria deveria ser atualizado")
        Assertions.assertEquals(updatedCategory.description, returnedCategory.description, "A descrição da categoria deveria ser atualizada")
    }

    @Test
    fun `Deve deletar categoria de conteudo existente`() {
        val categoryToDelete = Category(
            name = "Categoria para Deletar",
            description = "Esta categoria será deletada no teste.",
            auditable = false,
            createdAt = null
        )
        repository.save(categoryToDelete)

        val existingCategory = repository.findAll().last()

        val requestBuilder = MockMvcRequestBuilders
            .delete("/v1/content/category/${existingCategory.id}")
            .accept(MediaType.APPLICATION_JSON)

        mvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        val deletedCategory = repository.findById(existingCategory.id!!)
        Assertions.assertTrue(deletedCategory.isEmpty, "A categoria deveria ser deletada do repositório")
    }
}