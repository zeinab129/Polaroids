package com.ziziapps.polaroids.depenedencyInjection

import com.ziziapps.polaroids.repositories.EditImageRepository
import com.ziziapps.polaroids.repositories.EditImageRepositoryImpl
import com.ziziapps.polaroids.repositories.SavedImagesRepository
import com.ziziapps.polaroids.repositories.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository>{EditImageRepositoryImpl(androidContext())}
    factory<SavedImagesRepository>{SavedImagesRepositoryImpl(androidContext())}
}