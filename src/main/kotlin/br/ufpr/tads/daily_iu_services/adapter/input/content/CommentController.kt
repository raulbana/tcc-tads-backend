package br.ufpr.tads.daily_iu_services.adapter.input.content

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.LikeToggleDTO
import br.ufpr.tads.daily_iu_services.domain.service.CommentService
import com.azure.core.annotation.QueryParam
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
class CommentController(private val service: CommentService) {

    @PostMapping
    fun createComment(@RequestBody request: CommentCreatorDTO): ResponseEntity<CommentDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createComment(request))
    }

    @PutMapping("/{id}")
    fun updateComment(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long,
        @RequestBody request: CommentCreatorDTO
    ): ResponseEntity<CommentDTO> {
        return ResponseEntity.ok(service.updateComment(id, request, userId))
    }

    @GetMapping("/{id}")
    fun getComments(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long,
        @QueryParam("page") page: Int?
    ): ResponseEntity<List<CommentDTO>> {
        return ResponseEntity.ok(service.getCommentsByContentId(id, userId, page))
    }

    @GetMapping("/{id}/replies")
    fun getCommentReplies(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long,
        @QueryParam("page") page: Int?
    ): ResponseEntity<List<CommentDTO>> {
        return ResponseEntity.ok(service.getRepliesByCommentId(id, userId, page))
    }

    @PatchMapping("/{id}")
    fun likeComment(@PathVariable("id") id: Long, @RequestBody toggle: LikeToggleDTO): ResponseEntity<Void> {
        service.toggleLikeComment(id, toggle)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteComment(@PathVariable("id") id: Long): ResponseEntity<Void> {
        service.deleteComment(id)
        return ResponseEntity.noContent().build()
    }
}