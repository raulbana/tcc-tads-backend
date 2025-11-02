package br.ufpr.tads.daily_iu_services.adapter.input.content

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentRepostDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentUpdateDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ToggleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ReportContentDTO
import br.ufpr.tads.daily_iu_services.domain.service.ContentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/content")
@Tag(name = "Conteúdo", description = "Endpoints para gerenciar o conteúdo da rede social")
class ContentController(private val service: ContentService) {

    @PostMapping
    @Operation(summary = "Criar Conteúdo", description = "Criar uma nova publicação de conteúdo")
    @ApiResponse(responseCode = "201", description = "Conteúdo criado com sucesso")
    fun createContent(@RequestBody @Valid request: ContentCreatorDTO): ResponseEntity<ContentDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createContent(request))
    }

    @PostMapping("/{id}/repost")
    @Operation(summary = "Repostar Conteúdo", description = "Repostar uma publicação de conteúdo existente pelo ID")
    fun repostContent(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: ContentRepostDTO
    ): ResponseEntity<ContentDTO> {
        return ResponseEntity.ok(service.repostContent(id, request))
    }

    @GetMapping
    @Operation(
        summary = "Obter Conteúdos",
        description = "Recupera publicações de conteúdo para um usuário. Se o cabeçalho 'x-profile' for verdadeiro, busca conteúdos específicos do perfil com base no ID do usuário, caso contrário, busca publicações recomendadas para o usuário."
    )
    fun getContents(
        @RequestHeader(value = "x-user-id") userId: Long,
        @RequestHeader(value = "x-profile", required = false) profile: Boolean?,
        @RequestHeader(value = "page", required = false) page: Int?,
        @RequestHeader(value = "size", required = false) size: Int?
    ): ResponseEntity<List<ContentSimpleDTO>> {
        return ResponseEntity.ok(service.getContents(userId, profile ?: false, page, size))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter Conteúdo por ID",
        description = "Recupera uma publicação de conteúdo específica pelo seu ID"
    )
    fun getContentById(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long
    ): ResponseEntity<ContentDTO> {
        return ResponseEntity.ok(service.getContentById(id, userId))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Conteúdo", description = "Atualiza uma publicação de conteúdo existente pelo ID")
    fun updateContent(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: ContentUpdateDTO,
        @RequestHeader("x-user-id") userId: Long
    ): ResponseEntity<ContentDTO> {
        return ResponseEntity.ok(service.updateContent(request, id, userId))
    }

    @PatchMapping("/{id}")
    @Operation(
        summary = "Curtir/Descurtir Conteúdo",
        description = "Alterna o status de curtida para uma publicação de conteúdo específica pelo seu ID"
    )
    @ApiResponse(responseCode = "204", description = "Operação realizada com sucesso, sem conteúdo na resposta")
    fun toggleLikeContent(@PathVariable("id") id: Long, @RequestBody toggle: ToggleDTO): ResponseEntity<Void> {
        service.toggleLikeContent(id, toggle)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Conteúdo", description = "Deleta uma publicação de conteúdo pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Conteúdo deletado com sucesso, sem conteúdo na resposta")
    fun deleteContent(@PathVariable("id") id: Long): ResponseEntity<Void> {
        service.deleteContent(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/report")
    @Operation(summary = "Reportar Conteúdo", description = "Reportar uma publicação de conteúdo pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Report enviado com sucesso, sem conteúdo na resposta")
    fun reportContent(@PathVariable("id") id: Long, @RequestBody request: ReportContentDTO): ResponseEntity<Void> {
        service.reportContent(id, request)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/save")
    @Operation(
        summary = "Salvar/Remover dos Salvos",
        description = "Alterna o status de salvo para uma publicação de conteúdo específica pelo seu ID"
    )
    @ApiResponse(responseCode = "204", description = "Operação realizada com sucesso, sem conteúdo na resposta")
    fun toggleSaveContent(@PathVariable("id") id: Long, @RequestBody toggle: ToggleDTO): ResponseEntity<Void> {
        service.toggleSaveContent(id, toggle)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/saved")
    @Operation(
        summary = "Obter Conteúdos salvos",
        description = "Recupera publicações de conteúdo salvos por um usuário."
    )
    fun getSavedContents(@RequestHeader("x-user-id") userId: Long): ResponseEntity<List<ContentSimpleDTO>> {
        return ResponseEntity.ok(service.listSavedContents(userId))
    }
}