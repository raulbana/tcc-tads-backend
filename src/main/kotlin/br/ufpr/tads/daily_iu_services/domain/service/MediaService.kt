package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.MediaCreateDTO
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.Locale
import java.util.UUID

@Service
class MediaService {

    @Value("\${project.dailyiu.azure.storage.connection-string}")
    private lateinit var connectionString: String

    @Value("\${project.dailyiu.azure.storage.container-name}")
    private lateinit var containerName: String

    private lateinit var containerClient: BlobContainerClient

    private fun getBlobContainerClient(): BlobContainerClient {
        if (!::containerClient.isInitialized) {
            val blobServiceClient: BlobServiceClient = BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient()

            // Cria o container se não existir
            containerClient = blobServiceClient.getBlobContainerClient(containerName)
            if (!containerClient.exists()) {
                containerClient.create()
            }
        }
        return containerClient
    }

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

            try {
                val blobClient = getBlobContainerClient().getBlobClient(uniqueFilename)
                blobClient.upload(file.inputStream, file.size, true)
                media.add(MediaCreateDTO(blobClient.blobUrl, file.contentType, file.size))
            } catch (e: IOException) {
                throw RuntimeException("Falha ao fazer upload do arquivo $originalFilename", e)
            }
        }

        return media
    }

    private fun getFileExtension(filename: String?): String {
        if (filename == null || !filename.contains(".")) return ""
        return filename.substring(filename.lastIndexOf('.') + 1).lowercase(Locale.getDefault())
    }
}