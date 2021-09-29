package com.ziziapps.polaroids.viewModels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziziapps.polaroids.data.ImageFilter
import com.ziziapps.polaroids.repositories.EditImageRepository
import com.ziziapps.polaroids.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository): ViewModel() {

    //region:: Prepare Image Preview

    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri){
        Coroutines.io {
            kotlin.runCatching {
                emitUiPreviewState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null){
                    emitUiPreviewState(bitmap = bitmap)
                }else{
                    emitUiPreviewState(error = "Unable to prepare image preview")
                }
            }.onFailure {
                emitUiPreviewState(error = it.message.toString())
            }
        }
    }

    private fun emitUiPreviewState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ){
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )
    //endregion

    //region:: Load Image Filters
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUiState: LiveData<ImageFiltersDataState> get() = imageFiltersDataState

    fun loadImageFilters(originalImage: Bitmap){
        Coroutines.io {
            kotlin.runCatching {
                emitImageFiltersUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFiltersUiState(imageFilters = imageFilters)
            }.onFailure {
                emitImageFiltersUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage: Bitmap): Bitmap{
        return kotlin.runCatching {
            val previewWidth = 150
            val previewHigh = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewWidth, false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFiltersUiState(
        isLoading: Boolean = false,
        imageFilters: List<ImageFilter>? = null,
        error: String? = null
    ){
        val dataState = ImageFiltersDataState(isLoading, imageFilters, error)
        imageFiltersDataState.postValue(dataState)
    }


    data class ImageFiltersDataState(
        val isLoading: Boolean,
        val imageFilters: List<ImageFilter>?,
        val error: String?
    )
    //endregion

    //region:: Save Filtered Image
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap: Bitmap){
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepository.saveFilteredImage(filteredBitmap)
            }.onSuccess { savedImageUri ->
                emitSaveFilteredImageUiState(uri = savedImageUri)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }

    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )
    //endregion
}