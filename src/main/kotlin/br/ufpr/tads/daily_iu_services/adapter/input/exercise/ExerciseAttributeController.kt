package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseAttributeDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/exercise/attribute")
@Tag(name = "Atributo de Exercício", description = "Endpoints para gerenciamento de atributos de exercício (Beneficios, Contraindicações, Dicas)")
class ExerciseAttributeController {

    @PostMapping
    @Operation(summary = "Criar Atributo", description = "Cria um novo atributo de exercício")
    @ApiResponse(responseCode = "201", description = "Atributo criado com sucesso")
    fun createAttribute(@RequestBody @Valid request: ExerciseAttributeDTO): ResponseEntity<ExerciseAttributeDTO> {
        TODO("Not yet implemented")
    }

    @GetMapping
    @Operation(summary = "Buscar Todos Atributos", description = "Recupera todos os atributos de exercício")
    fun getAllAttributes(): ResponseEntity<List<ExerciseAttributeDTO>> {
        TODO("Not yet implemented")
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Atributo", description = "Atualiza um atributo de exercício existente pelo ID")
    fun updateAttribute(@RequestBody @Valid request: ExerciseAttributeDTO): ResponseEntity<ExerciseAttributeDTO> {
        TODO("Not yet implemented")
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Atributo", description = "Exclui um atributo de exercício pelo ID")
    @ApiResponse(responseCode = "204", description = "Atributo excluído com sucesso")
    fun deleteAttribute() {
        TODO("Not yet implemented")
    }
}