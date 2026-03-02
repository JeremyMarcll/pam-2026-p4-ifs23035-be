package org.delcom.module

import org.delcom.repositories.ISerpentRepository
import org.delcom.repositories.SerpentRepository
import org.delcom.repositories.IPlantRepository
import org.delcom.repositories.PlantRepository
import org.delcom.services.SerpentService
import org.delcom.services.PlantService
import org.delcom.services.ProfileService
import org.koin.dsl.module

val appModule = module {

    // ======================
    // SERPENT
    // ======================
    single<ISerpentRepository> {
        SerpentRepository()
    }

    single {
        SerpentService(get())
    }

    // ======================
    // PLANT
    // ======================
    single<IPlantRepository> {
        PlantRepository()
    }

    single {
        PlantService(get())
    }

    // ======================
    // PROFILE
    // ======================
    single {
        ProfileService()
    }
}