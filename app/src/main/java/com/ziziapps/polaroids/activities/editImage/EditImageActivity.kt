package com.ziziapps.polaroids.activities.editImage

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ziziapps.polaroids.activities.filteredImage.FilteredImageActivity
import com.ziziapps.polaroids.activities.main.MainActivity
import com.ziziapps.polaroids.adapters.ImageFiltersAdapter
import com.ziziapps.polaroids.data.ImageFilter
import com.ziziapps.polaroids.databinding.ActivityEditImageBinding
import com.ziziapps.polaroids.listeners.ImageFilterListener
import com.ziziapps.polaroids.utilities.displayToast
import com.ziziapps.polaroids.utilities.show
import com.ziziapps.polaroids.viewModels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    //Image bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setUpObservers()
        prepareImagePreview()
    }

    private fun setUpObservers(){
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it?: return@observe
            binding.activityEditImagePbLoading.visibility =
                if(dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                //For the first time 'filtered image = original image'
                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.activityEditImageIvPreview.show()
                    viewModel.loadImageFilters(this)
                }
            }?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error )
                }
            }
        })

        viewModel.imageFiltersUiState.observe(this, {
            val imageFiltersDataState = it?: return@observe
            binding.activityEditImagePbFilterLoading.visibility =
                if(imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.activityEditImageRvFilters.adapter = adapter
                }
            }?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })

        filteredBitmap.observe(this, { bitmap ->
            binding.activityEditImageIvPreview.setImageBitmap(bitmap)
        })

        viewModel.saveFilteredImageUiState.observe(this, {
            val saveFilteredImageDataState = it?: return@observe
            if (saveFilteredImageDataState.isLoading){
                binding.activityEditImageTvSave.visibility = View.GONE
                binding.activityEditImagePbSaving.visibility = View.VISIBLE
            }else{
                binding.activityEditImageTvSave.visibility = View.VISIBLE
                binding.activityEditImagePbSaving.visibility = View.GONE
            }
            saveFilteredImageDataState.uri?.let { savedImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filteredImageIntent)
                }
            }?: kotlin.run{
                saveFilteredImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }


    private fun setListeners(){
        binding.activityEditImageIvBack.setOnClickListener{
            onBackPressed()
        }

        binding.activityEditImageTvSave.setOnClickListener{
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }

        /*
        This will show original image when we long click the imageView until we release click,
        So that we can see difference between original image and filtered image
         */

        binding.activityEditImageIvPreview.setOnLongClickListener {
            binding.activityEditImageIvPreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.activityEditImageIvPreview.setOnClickListener{
            binding.activityEditImageIvPreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }


}