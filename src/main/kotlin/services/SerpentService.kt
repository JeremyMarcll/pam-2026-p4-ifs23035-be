package org.delcom.services

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.receiveMultipart
import org.delcom.data.SerpentRequest
import io.ktor.server.response.*
import io.ktor.util.cio.*
import io.ktor.utils.io.copyAndClose
import org.delcom.data.AppException
import org.delcom.data.DataResponse
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.ISerpentRepository
import java.io.File
import java.util.*

class SerpentService(private val serpentRepository: ISerpentRepository) {

    // ======================
    // GET ALL
    // ======================
    suspend fun getSerpents(call: ApplicationCall) {

        val search = call.request.queryParameters["search"] ?: ""

        val serpents = serpentRepository.getSerpents(search)

        call.respond(
            DataResponse(
                "success",
                "Berhasil mengambil daftar serpent",
                mapOf("serpents" to serpents)
            )
        )
    }

    // ======================
    // GET BY ID
    // ======================
    suspend fun getSerpentById(call: ApplicationCall) {

        val id = call.parameters["id"]
            ?: throw AppException(400, "ID serpent tidak boleh kosong!")

        val serpent = serpentRepository.getSerpentById(id)
            ?: throw AppException(404, "Data serpent tidak tersedia!")

        call.respond(
            DataResponse(
                "success",
                "Berhasil mengambil data serpent",
                mapOf("serpent" to serpent)
            )
        )
    }

    // ======================
    // MULTIPART REQUEST
    // ======================
    private suspend fun getSerpentRequest(call: ApplicationCall): SerpentRequest {

        val serpentReq = SerpentRequest()

        val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 5)

        multipartData.forEachPart { part ->

            when (part) {

                is PartData.FormItem -> {
                    when (part.name) {
                        "nama" -> serpentReq.nama = part.value.trim()
                        "deskripsi" -> serpentReq.deskripsi = part.value
                        "kekuatanPeran" -> serpentReq.kekuatanPeran = part.value
                        "simbolisme" -> serpentReq.simbolisme = part.value
                    }
                }

                is PartData.FileItem -> {

                    val ext = part.originalFileName
                        ?.substringAfterLast('.', "")
                        ?.let { if (it.isNotEmpty()) ".$it" else "" }
                        ?: ""

                    val fileName = UUID.randomUUID().toString() + ext
                    val filePath = "uploads/serpents/$fileName"

                    val file = File(filePath)
                    file.parentFile.mkdirs()

                    part.provider().copyAndClose(file.writeChannel())

                    serpentReq.gambar = filePath
                }

                else -> {}
            }

            part.dispose()
        }

        return serpentReq
    }

    // ======================
    // VALIDATION
    // ======================
    private fun validateSerpentRequest(serpentReq: SerpentRequest) {

        val validator = ValidatorHelper(serpentReq.toMap())

        validator.required("nama", "Nama tidak boleh kosong")
        validator.required("deskripsi", "Deskripsi tidak boleh kosong")
        validator.required("kekuatanPeran", "Kekuatan atau peran tidak boleh kosong")
        validator.required("simbolisme", "Simbolisme tidak boleh kosong")
        validator.required("gambar", "Gambar tidak boleh kosong")

        validator.validate()

        val file = File(serpentReq.gambar)
        if (!file.exists()) {
            throw AppException(400, "Gambar serpent gagal diupload!")
        }
    }

    // ======================
    // CREATE
    // ======================
    suspend fun createSerpent(call: ApplicationCall) {

        val serpentReq = getSerpentRequest(call)

        validateSerpentRequest(serpentReq)

        val existSerpent = serpentRepository.getSerpentByName(serpentReq.nama)

        if (existSerpent != null) {
            File(serpentReq.gambar).delete()
            throw AppException(409, "Serpent dengan nama ini sudah terdaftar!")
        }

        val serpentId = serpentRepository.addSerpent(
            serpentReq.toEntity()
        )

        call.respond(
            DataResponse(
                "success",
                "Berhasil menambahkan serpent",
                mapOf("serpentId" to serpentId)
            )
        )
    }

    // ======================
    // UPDATE
    // ======================
    suspend fun updateSerpent(call: ApplicationCall) {

        val id = call.parameters["id"]
            ?: throw AppException(400, "ID serpent tidak boleh kosong!")

        val oldSerpent = serpentRepository.getSerpentById(id)
            ?: throw AppException(404, "Data serpent tidak tersedia!")

        val serpentReq = getSerpentRequest(call)

        if (serpentReq.gambar.isEmpty()) {
            serpentReq.gambar = oldSerpent.gambar
        }

        validateSerpentRequest(serpentReq)

        if (serpentReq.nama != oldSerpent.nama) {

            val existSerpent = serpentRepository.getSerpentByName(serpentReq.nama)

            if (existSerpent != null) {
                File(serpentReq.gambar).delete()
                throw AppException(409, "Serpent dengan nama ini sudah terdaftar!")
            }
        }

        if (serpentReq.gambar != oldSerpent.gambar) {
            File(oldSerpent.gambar).delete()
        }

        val isUpdated = serpentRepository.updateSerpent(
            id,
            serpentReq.toEntity()
        )

        if (!isUpdated) {
            throw AppException(400, "Gagal memperbarui data serpent!")
        }

        call.respond(
            DataResponse(
                "success",
                "Berhasil mengubah data serpent",
                null
            )
        )
    }

    // ======================
    // DELETE
    // ======================
    suspend fun deleteSerpent(call: ApplicationCall) {

        val id = call.parameters["id"]
            ?: throw AppException(400, "ID serpent tidak boleh kosong!")

        val oldSerpent = serpentRepository.getSerpentById(id)
            ?: throw AppException(404, "Data serpent tidak tersedia!")

        val isDeleted = serpentRepository.removeSerpent(id)

        if (!isDeleted) {
            throw AppException(400, "Gagal menghapus data serpent!")
        }

        File(oldSerpent.gambar).delete()

        call.respond(
            DataResponse(
                "success",
                "Berhasil menghapus data serpent",
                null
            )
        )
    }

    // ======================
    // GET IMAGE
    // ======================
    suspend fun getSerpentImage(call: ApplicationCall) {

        val id = call.parameters["id"]
            ?: return call.respond(HttpStatusCode.BadRequest)

        val serpent = serpentRepository.getSerpentById(id)
            ?: return call.respond(HttpStatusCode.NotFound)

        val file = File(serpent.gambar)

        if (!file.exists()) {
            return call.respond(HttpStatusCode.NotFound)
        }

        call.respondFile(file)
    }
}