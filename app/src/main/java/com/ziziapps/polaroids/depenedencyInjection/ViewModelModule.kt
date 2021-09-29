package com.ziziapps.polaroids.depenedencyInjection

import com.ziziapps.polaroids.viewModels.EditImageViewModel
import com.ziziapps.polaroids.viewModels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {EditImageViewModel(editImageRepository = get())}
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}