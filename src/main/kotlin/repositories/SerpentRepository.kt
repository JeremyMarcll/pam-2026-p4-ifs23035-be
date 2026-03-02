package org.delcom.repositories

import org.delcom.dao.SerpentDAO
import org.delcom.entities.Serpent
import org.delcom.helpers.serpentDaoToModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.SerpentTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import java.util.UUID

class SerpentRepository : ISerpentRepository {

    override suspend fun getSerpents(search: String): List<Serpent> = suspendTransaction {
        if (search.isBlank()) {
            SerpentDAO.all()
                .orderBy(SerpentTable.createdAt to SortOrder.DESC)
                .limit(20)
                .map(::serpentDaoToModel)
        } else {
            val keyword = "%${search.lowercase()}%"

            SerpentDAO
                .find {
                    SerpentTable.nama.lowerCase() like keyword
                }
                .orderBy(SerpentTable.nama to SortOrder.ASC)
                .limit(20)
                .map(::serpentDaoToModel)
        }
    }

    override suspend fun getSerpentById(id: String): Serpent? = suspendTransaction {
        SerpentDAO
            .find { (SerpentTable.id eq UUID.fromString(id)) }
            .limit(1)
            .map(::serpentDaoToModel)
            .firstOrNull()
    }

    override suspend fun getSerpentByName(name: String): Serpent? = suspendTransaction {
        SerpentDAO
            .find { (SerpentTable.nama eq name) }
            .limit(1)
            .map(::serpentDaoToModel)
            .firstOrNull()
    }

    override suspend fun addSerpent(serpent: Serpent): String = suspendTransaction {
        val serpentDAO = SerpentDAO.new {
            nama = serpent.nama
            gambar = serpent.gambar
            deskripsi = serpent.deskripsi
            kekuatanPeran = serpent.kekuatanPeran
            simbolisme = serpent.simbolisme
            createdAt = serpent.createdAt
            updatedAt = serpent.updatedAt
        }

        serpentDAO.id.value.toString()
    }

    override suspend fun updateSerpent(id: String, newSerpent: Serpent): Boolean = suspendTransaction {
        val serpentDAO = SerpentDAO
            .find { SerpentTable.id eq UUID.fromString(id) }
            .limit(1)
            .firstOrNull()

        if (serpentDAO != null) {
            serpentDAO.nama = newSerpent.nama
            serpentDAO.gambar = newSerpent.gambar
            serpentDAO.deskripsi = newSerpent.deskripsi
            serpentDAO.kekuatanPeran = newSerpent.kekuatanPeran
            serpentDAO.simbolisme = newSerpent.simbolisme
            serpentDAO.updatedAt = newSerpent.updatedAt
            true
        } else {
            false
        }
    }

    override suspend fun removeSerpent(id: String): Boolean = suspendTransaction {
        val rowsDeleted = SerpentTable.deleteWhere {
            SerpentTable.id eq UUID.fromString(id)
        }
        rowsDeleted == 1
    }
}