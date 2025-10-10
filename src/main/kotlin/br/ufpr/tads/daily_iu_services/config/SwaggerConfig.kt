package br.ufpr.tads.daily_iu_services.config

import br.ufpr.tads.daily_iu_services.exception.domain.Message
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.core.converter.ResolvedSchema
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import java.util.function.Consumer

@Configuration
@EnableWebMvc
@ConditionalOnProperty(name = ["swagger.enabled"], havingValue = "true", matchIfMissing = true)
class SwaggerConfig {

    @Value("\${project.info.app.name}")
    lateinit var appName: String

    @Value("\${project.info.app.description}")
    lateinit var appDescription: String

    @Value("\${project.info.app.version}")
    lateinit var appVersion: String

    @Override
    fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/swagger-ui/")
            .setViewName("forward:"+"/swagger-ui/index.html")
    }

    @Bean
    fun springShopOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title(appName)
                    .description(appDescription)
                    .version(appVersion)
            )
    }

    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI? ->
            openApi!!.components
                .schemas[Message::class.java.getSimpleName()] = loadSchema(Message::class.java).schema
            openApi.paths.values.forEach(
                Consumer { pathItem: PathItem? ->
                    pathItem!!.readOperations().forEach(Consumer { operation: Operation? ->
                        operation!!.responses
                            .addApiResponse("400", registerResponse("Bad Request", Message::class.java))
                            .addApiResponse("401", registerResponse("Unauthorized", Message::class.java))
                            .addApiResponse("403", registerResponse("Forbidden", Message::class.java))
                            .addApiResponse("404", registerResponse("Not Found", Message::class.java))
                            .addApiResponse("500", registerResponse("Internal Server Error", Message::class.java))
                            .addApiResponse("501", registerResponse("Not Implemented", Message::class.java))
                    })
                }
            )
        }
    }

    private fun loadSchema(clazz: Class<*>): ResolvedSchema {
        return ModelConverters.getInstance().resolveAsResolvedSchema(AnnotatedType(clazz))
    }

    private fun registerResponse(description: String, clazz: Class<*>): ApiResponse {
        val schema = loadSchema(clazz).schema
        val content = Content().addMediaType("application/json", MediaType().schema(schema))
        return ApiResponse().description(description).content(content)
    }

}