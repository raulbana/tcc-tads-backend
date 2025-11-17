package br.ufpr.tads.daily_iu_services.adapter.input

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
class CalendarControllerTest() {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `Deve retornar 200 ao buscar eventos do calendario para usuario 2`() {
        mvc.perform(MockMvcRequestBuilders.get("/v1/calendar")
            .header("x-user-id", "2")
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

}