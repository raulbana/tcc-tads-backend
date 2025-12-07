package br.ufpr.tads.daily_iu_services.adapter.output.media

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream

@Service
class AzureBSClient {

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

    fun upload(file: MultipartFile, uniqueFilename: String, originalFilename: String): String{
        try {
            val blobClient = getBlobContainerClient().getBlobClient(uniqueFilename)
            blobClient.upload(file.inputStream, file.size, true)

            return blobClient.blobUrl
        } catch (e: IOException) {
            throw RuntimeException("Falha ao fazer upload do arquivo $originalFilename", e)
        }
    }

    fun upload(file: InputStream, fileSize: Long, uniqueFilename: String, originalFilename: String): String{
        try {
            val blobClient = getBlobContainerClient().getBlobClient(uniqueFilename)
            blobClient.upload(file, fileSize, true)

            return blobClient.blobUrl
        } catch (e: IOException) {
            throw RuntimeException("Falha ao fazer upload do arquivo $originalFilename", e)
        }
    }
}