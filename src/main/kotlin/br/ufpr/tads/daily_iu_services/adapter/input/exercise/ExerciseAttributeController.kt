package br.ufpr.tads.daily_iu_services.adapter.input.exercise

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseAttributeDTO
import br.ufpr.tads.daily_iu_services.domain.service.ExerciseAttributeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/exercise/attribute")
@Tag(name = "Atributo de Exercício", description = "Endpoints para gerenciamento de atributos de exercício (Beneficios, Contraindicações, Dicas)")
class ExerciseAttributeController(val service: ExerciseAttributeService) {

    @PostMapping
    @Operation(summary = "Criar Atributo", description = "Cria um novo atributo de exercício")
    @ApiResponse(responseCode = "201", description = "Atributo criado com sucesso")
    fun createAttribute(@RequestBody @Valid request: ExerciseAttributeDTO): ResponseEntity<ExerciseAttributeDTO> {
        return ResponseEntity.status(201).body(service.createAttribute(request))
    }

    @GetMapping
    @Operation(summary = "Buscar Todos Atributos", description = "Recupera todos os atributos de exercício")
    fun getAllAttributes(): ResponseEntity<List<ExerciseAttributeDTO>> {
        return ResponseEntity.ok(service.getAllAttributes())
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Atributo", description = "Atualiza um atributo de exercício existente pelo ID")
    fun updateAttribute(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: ExerciseAttributeDTO
    ): ResponseEntity<ExerciseAttributeDTO> {
        return ResponseEntity.ok(service.updateAttribute(id, request))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Atributo", description = "Exclui um atributo de exercício pelo ID")
    @ApiResponse(responseCode = "204", description = "Atributo excluído com sucesso")
    fun deleteAttribute(@PathVariable("id") id: Long): ResponseEntity<Void> {
        service.deleteAttribute(id)
        return ResponseEntity.noContent().build()
    }
}