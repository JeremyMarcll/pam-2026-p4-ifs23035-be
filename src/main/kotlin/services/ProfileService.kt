package org.delcom.services

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.delcom.data.DataResponse
import java.io.File

class ProfileService {

    // Mengambil data profile pengembang
    suspend fun getProfile(call: ApplicationCall) {

        val response = DataResponse(
            "success",
            "Berhasil mengambil profile pengembang aplikasi daftar Makhluk Mitologi tipe Serpent",
            mapOf(
                "username" to "jeremy.manullang",
                "nama" to "Jeremy Manullang",
                "tentang" to "Saya adalah developer aplikasi daftar Makhluk Mitologi berbasis Ktor. Aplikasi ini memungkinkan pengguna untuk melihat informasi alat musik seperti asal musik, deskripsi, dan cara memainkannya. Saya tertarik pada backend development, REST API, dan pengelolaan database modern.daftar Makhluk Mitologi bertipe Serpent berbasis Ktor. Aplikasi ini memungkinkan pengguna untuk melihat informasi makhluk serpent seperti deskripsi, kekuatan atau peran, simbolisme, serta gambar dari setiap makhluk mitologi. Saya tertarik pada backend development, pengembangan REST API, dan pengelolaan database modern."
            )
        )

        call.respond(response)
    }

    // Mengambil foto profile
    suspend fun getProfilePhoto(call: ApplicationCall) {

        val file = File("uploads/profile/me.png")

        if (!file.exists()) {
            return call.respond(HttpStatusCode.NotFound)
        }

        call.respondFile(file)
    }
}