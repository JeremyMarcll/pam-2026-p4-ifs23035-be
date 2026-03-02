package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.SerpentDAO
import org.delcom.dao.PlantDAO
import org.delcom.entities.Serpent
import org.delcom.entities.Plant
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

// =========================
// TRANSACTION SUSPEND
// =========================
suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)


// =========================
// SERPENT MAPPING
// =========================
fun serpentDaoToModel(dao: SerpentDAO) = Serpent(
    id = dao.id.value.toString(),
    nama = dao.nama,
    gambar = dao.gambar,
    deskripsi = dao.deskripsi,
    kekuatanPeran = dao.kekuatanPeran,
    simbolisme = dao.simbolisme,
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)


// =========================
// PLANT MAPPING
// =========================
fun plantDaoToModel(dao: PlantDAO) = Plant(
    id = dao.id.value.toString(),
    nama = dao.nama,
    pathGambar = dao.pathGambar,
    deskripsi = dao.deskripsi,
    manfaat = dao.manfaat,
    efekSamping = dao.efekSamping,
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)