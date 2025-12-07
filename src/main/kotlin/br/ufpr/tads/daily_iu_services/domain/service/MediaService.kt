package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaCreateDTO
import br.ufpr.tads.daily_iu_services.adapter.output.media.AzureBSClient
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class MediaService(private val azureClient: AzureBSClient) {

    /**
     * Faz upload de arquivos para o Azure Blob Storage
     * @param files Array de arquivos para upload
     * @return Lista com as URLs públicas dos arquivos
     */
    fun upload(files: Array<MultipartFile>): List<MediaCreateDTO> {
        val media = mutableListOf<MediaCreateDTO>()

        for (file in files) {
            if (file.isEmpty) continue

            val originalFilename = file.originalFilename ?: "file-${UUID.randomUUID()}"
            val extension = getFileExtension(originalFilename)
            val uniqueFilename = "${UUID.randomUUID()}.$extension"

            val blobUrl = azureClient.upload(file, uniqueFilename, originalFilename)
            media.add(MediaCreateDTO(blobUrl, file.contentType, file.size))

            if (verifyFileIsAVideo(file)) {
                val coverInputStream = extractCoverImageFromVideo(file)
                if (coverInputStream != null) {
                    val coverUniqueFilename = "${UUID.randomUUID()}.jpg"
                    val coverBytes = coverInputStream.readAllBytes()
                    val coverSize = coverBytes.size.toLong()

                    val coverBlobUrl = azureClient.upload(ByteArrayInputStream(coverBytes), coverSize, coverUniqueFilename, "cover.jpg")
                    media.add(MediaCreateDTO(coverBlobUrl, "image/jpeg", coverSize))
                }
            }
        }

        return media
    }

    private fun getFileExtension(filename: String?): String {
        if (filename == null || !filename.contains(".")) return ""
        return filename.substring(filename.lastIndexOf('.') + 1).lowercase(Locale.getDefault())
    }

    private fun verifyFileIsAVideo(file: MultipartFile): Boolean {
        return file.contentType?.contains("video") ?: false
    }

    private fun extractCoverImageFromVideo(file: MultipartFile): InputStream? {
        var tempFile: File? = null
        var tempImage: File? = null

        return try {
            // salva em arquivo temporário (vídeo)
            tempFile = File.createTempFile("video-", ".${getFileExtension(file.originalFilename)}")
            file.inputStream.use { input ->
                Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }

            // cria arquivo temporário para imagem
            tempImage = File.createTempFile("cover-", ".jpg")

            // comando ffmpeg: seek em 1s e extrai um frame
            val command = listOf(
                "ffmpeg",
                "-y",
                "-ss", "1",
                "-i", tempFile.absolutePath,
                "-frames:v", "1",
                "-q:v", "2",
                tempImage.absolutePath
            )

            val process = ProcessBuilder(command)
                .redirectErrorStream(true)
                .start()

            // aguarda com timeout (10s)
            val finished = process.waitFor(10, TimeUnit.SECONDS)
            if (!finished || process.exitValue() != 0) {
                // se falhou, tenta ler saída para debug (não lança exceção)
                try {
                    val err = process.inputStream.readAllBytes().toString(Charsets.UTF_8)
                    System.err.println("ffmpeg failed: $err")
                } catch (_: Exception) {}
                return null
            }

            // lê bytes da imagem criada
            val bytes = Files.readAllBytes(tempImage.toPath())
            ByteArrayInputStream(bytes)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        } finally {
            try { tempFile?.delete() } catch (_: Exception) {}
            try { tempImage?.delete() } catch (_: Exception) {}
        }
    }
}