package br.ufpr.tads.daily_iu_services.adapter.input.media

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaCreateDTO
import br.ufpr.tads.daily_iu_services.domain.service.MediaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v1/media")
@Tag(name = "Mídia", description = "Endpoints para gerenciar uploads de mídia")
class MediaController(private val mediaService: MediaService) {

    @PostMapping("/upload")
    @Operation(summary = "Enviar Mídia", description = "Envie um ou mais arquivos de mídia")
    fun uploadMedia(@RequestParam("files") files: Array<MultipartFile>): ResponseEntity<List<MediaCreateDTO>> {
        return ResponseEntity.ok(mediaService.upload(files))
    }
}