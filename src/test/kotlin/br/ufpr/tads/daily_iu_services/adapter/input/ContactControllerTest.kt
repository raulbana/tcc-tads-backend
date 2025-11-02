package br.ufpr.tads.daily_iu_services.adapter.input

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
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
class ContactControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc


    companion object {
        private lateinit var greenMail: GreenMail

        @BeforeAll
        @JvmStatic
        fun setup() {
            greenMail = GreenMail(ServerSetup(3025, null, "smtp"))
            greenMail.start()
        }

        @AfterAll
        @JvmStatic
        fun teardown() {
            greenMail.stop()
        }
    }

    @Test
    fun `Deve enviar email de contato`() {
        mvc.perform(MockMvcRequestBuilders.post("/v1/contact/support")
            .content("{\"userEmail\": \"leosalgado2004@gmail.com\", \"subject\": \"Email Teste\", \"text\": \"Conteudo do email\"}")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)

        // Verifica se o email foi recebido
        val messages = greenMail.receivedMessages
        Assertions.assertTrue(messages.isNotEmpty(), "Nenhum email foi recebido")
        Assertions.assertEquals("Email Teste", messages[0].subject)
    }
}