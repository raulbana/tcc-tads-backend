package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaCreateDTO
import br.ufpr.tads.daily_iu_services.adapter.output.media.AzureBSClient
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Locale
import java.util.UUID
import javax.imageio.ImageIO

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
                    val coverSize = coverInputStream.available().toLong()

                    val coverBlobUrl = azureClient.upload(coverInputStream, coverSize, coverUniqueFilename, "cover.jpg")
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
        var grabber: FFmpegFrameGrabber? = null

        return try {
            // salva em arquivo temporário
            tempFile = File.createTempFile("video-", "." + getFileExtension(file.originalFilename))
            file.inputStream.use { input ->
                Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }

            // cria e inicia o grabber (usar caminho evita passar opções indesejadas ao avformat_open_input)
            grabber = FFmpegFrameGrabber(tempFile.absolutePath)
            grabber.start()

            // posiciona em 1 segundo (timestamp em microssegundos)
            val targetTimestampUs = 1_000_000L
            if (grabber.lengthInTime > 0 && targetTimestampUs < grabber.lengthInTime) {
                grabber.timestamp = targetTimestampUs
            }

            // tenta capturar um frame de imagem
            val frame = grabber.grabImage() ?: grabber.grab() ?: return null

            // converte para BufferedImage
            val converter = Java2DFrameConverter()
            val buffered: BufferedImage = converter.convert(frame) ?: return null

            // escreve JPEG em bytes e retorna InputStream
            val baos = ByteArrayOutputStream()
            ImageIO.write(buffered, "jpg", baos)
            ByteArrayInputStream(baos.toByteArray())
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        } finally {
            try { grabber?.stop(); grabber?.release() } catch (_: Exception) {}
            try { tempFile?.delete() } catch (_: Exception) {}
        }
    }
}