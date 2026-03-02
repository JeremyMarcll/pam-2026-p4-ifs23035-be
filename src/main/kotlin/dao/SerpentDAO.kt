package org.delcom.dao

import org.delcom.tables.SerpentTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import java.util.UUID

class SerpentDAO(id: EntityID<UUID>) : Entity<UUID>(id) {

    companion object : EntityClass<UUID, SerpentDAO>(SerpentTable)

    var nama by SerpentTable.nama
    var gambar by SerpentTable.gambar
    var deskripsi by SerpentTable.deskripsi
    var kekuatanPeran by SerpentTable.kekuatanPeran
    var simbolisme by SerpentTable.simbolisme
    var createdAt by SerpentTable.createdAt
    var updatedAt by SerpentTable.updatedAt
}