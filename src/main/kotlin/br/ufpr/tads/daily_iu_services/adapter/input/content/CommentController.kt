package br.ufpr.tads.daily_iu_services.adapter.input.content

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ToggleDTO
import br.ufpr.tads.daily_iu_services.domain.service.CommentService
import com.azure.core.annotation.QueryParam
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
@RequestMapping("/v1/content/comments")
@Tag(name = "Comentários", description = "Endpoints para gerenciar comentários em conteúdos")
class CommentController(private val service: CommentService) {

    @PostMapping
    @Operation(summary = "Criar Comentário", description = "Cria um novo comentário em um conteúdo")
    @ApiResponse(responseCode = "201", description = "Comentário criado com sucesso")
    fun createComment(@RequestBody request: CommentCreatorDTO): ResponseEntity<CommentDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createComment(request))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Comentário", description = "Atualiza um comentário existente pelo ID")
    fun updateComment(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long,
        @RequestBody request: CommentCreatorDTO
    ): ResponseEntity<CommentDTO> {
        return ResponseEntity.ok(service.updateComment(id, request, userId))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comentários por ID do conteúdo", description = "Recupera comentários de um item de conteúdo específico pelo seu ID, com paginação opcional")
    fun getComments(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long,
        @QueryParam("page") page: Int?
    ): ResponseEntity<List<CommentDTO>> {
        return ResponseEntity.ok(service.getCommentsByContentId(id, userId, page))
    }

    @GetMapping("/{id}/replies")
    @Operation(summary = "Obter respostas de comentário", description = "Recupera respostas para um comentário específico pelo seu ID, com paginação opcional")
    fun getCommentReplies(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long,
        @QueryParam("page") page: Int?
    ): ResponseEntity<List<CommentDTO>> {
        return ResponseEntity.ok(service.getRepliesByCommentId(id, userId, page))
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Curtir/Descurtir Comentário", description = "Alterna o status de curtida para um comentário específico pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Status de curtida alterado com sucesso")
    fun likeComment(@PathVariable("id") id: Long, @RequestBody toggle: ToggleDTO): ResponseEntity<Void> {
        service.toggleLikeComment(id, toggle)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Comentário", description = "Deleta um comentário pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Comentário deletado com sucesso")
    fun deleteComment(@PathVariable("id") id: Long): ResponseEntity<Void> {
        service.deleteComment(id)
        return ResponseEntity.noContent().build()
    }
}