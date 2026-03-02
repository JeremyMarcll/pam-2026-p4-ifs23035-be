package org.delcom.data

import kotlinx.serialization.Serializable
import org.delcom.entities.Serpent

@Serializable
data class SerpentRequest(
    var nama: String = "",
    var deskripsi: String = "",
    var kekuatanPeran: String = "",
    var simbolisme: String = "",
    var gambar: String = ""
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nama" to nama,
            "deskripsi" to deskripsi,
            "kekuatanPeran" to kekuatanPeran,
            "simbolisme" to simbolisme,
            "gambar" to gambar
        )
    }

    fun toEntity(): Serpent {
        return Serpent(
            nama = nama,
            deskripsi = deskripsi,
            kekuatanPeran = kekuatanPeran,
            simbolisme = simbolisme,
            gambar = gambar
        )
    }
}