package com.ziziapps.polaroids.activities.savedImages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.content.IntentCompat
import com.ziziapps.polaroids.activities.editImage.EditImageActivity
import com.ziziapps.polaroids.activities.filteredImage.FilteredImageActivity
import com.ziziapps.polaroids.adapters.SavedImagesAdapter
import com.ziziapps.polaroids.databinding.ActivitySavedImagesBinding
import com.ziziapps.polaroids.listeners.SavedImageListener
import com.ziziapps.polaroids.utilities.displayToast
import com.ziziapps.polaroids.viewModels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity(), SavedImageListener {

    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setUpObserver(){
        viewModel.savedImagesUiState.observe(this, {
            val savedImageDataState = it ?: return@observe
            binding.activitySavedImagesPbLoading.visibility =
                if(savedImageDataState.isLoading) View.VISIBLE else View.GONE
            savedImageDataState.savedImages?.let { savedImages ->
                SavedImagesAdapter(savedImages, this).also { adapter ->
                    with(binding.activitySavedImagesRvSavedImages){
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: kotlin.run {
                savedImageDataState.error?.let { error->
                    displayToast(error)
                }
            }
        })
    }

    private fun setListeners(){
        binding.activitySavedImagesIvBack.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also { filteredImageIntent ->
            filteredImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(filteredImageIntent)
        }
    }
}