package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SerpentTable : UUIDTable("serpents") {

    val nama = varchar("nama", 100)
    val gambar = varchar("gambar", 255)
    val deskripsi = text("deskripsi")
    val kekuatanPeran = text("kekuatan_peran")
    val simbolisme = text("simbolisme")

    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}