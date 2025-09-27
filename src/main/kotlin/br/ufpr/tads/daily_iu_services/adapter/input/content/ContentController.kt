package br.ufpr.tads.daily_iu_services.adapter.input.content

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentRepostDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentSimpleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ContentUpdateDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.LikeToggleDTO
import br.ufpr.tads.daily_iu_services.domain.service.ContentService
import jakarta.validation.Valid
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
class ContentController(private val service: ContentService) {

    @PostMapping
    fun createContent(@RequestBody @Valid request: ContentCreatorDTO): ResponseEntity<ContentDTO> {
        return ResponseEntity.ok(service.createContent(request))
    }

    @PostMapping("/repost/{id}")
    fun repostContent(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: ContentRepostDTO): ResponseEntity<ContentDTO> {
        return ResponseEntity.ok(service.repostContent(id, request))
    }

    @GetMapping
    fun getContents(@RequestHeader("x-user-id") userId: Long): ResponseEntity<List<ContentSimpleDTO>> {
        return ResponseEntity.ok(service.getContents(userId))
    }

    @GetMapping("/{id}")
    fun getContentById(
        @PathVariable("id") id: Long,
        @RequestHeader("x-user-id") userId: Long
    ): ResponseEntity<ContentDTO> {
        return ResponseEntity.ok(service.getContentById(id, userId))
    }

    @PutMapping("/{id}")
    fun updateContent(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: ContentUpdateDTO,
        @RequestHeader("x-user-id") userId: Long
    ): ResponseEntity<ContentDTO> {
        return ResponseEntity.ok(service.updateContent(request, id, userId))
    }

    @PatchMapping("/{id}")
    fun toggleLikeContent(@PathVariable("id") id: Long, @RequestBody toggle: LikeToggleDTO): ResponseEntity<Void> {
        service.toggleLikeContent(id, toggle)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteContent(@PathVariable("id") id: Long): ResponseEntity<Void> {
        service.deleteContent(id)
        return ResponseEntity.noContent().build()
    }
}