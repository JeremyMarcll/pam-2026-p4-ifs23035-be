package org.delcom.repositories

import org.delcom.entities.Serpent

interface ISerpentRepository {

    suspend fun getSerpents(search: String): List<Serpent>

    suspend fun getSerpentById(id: String): Serpent?

    suspend fun getSerpentByName(name: String): Serpent?

    suspend fun addSerpent(serpent: Serpent): String

    suspend fun updateSerpent(id: String, newSerpent: Serpent): Boolean

    suspend fun removeSerpent(id: String): Boolean
}