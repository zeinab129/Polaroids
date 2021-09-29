package com.ziziapps.polaroids.activities.filteredImage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ziziapps.polaroids.R
import com.ziziapps.polaroids.activities.editImage.EditImageActivity
import com.ziziapps.polaroids.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {

    private lateinit var fileUri: Uri
    private lateinit var binding: ActivityFilteredImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayFilteredImage()
        setListeners()
    }

    private fun displayFilteredImage(){
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let { imageUri ->
            fileUri = imageUri
            binding.activityFilteredImageIvFilteredImage.setImageURI(fileUri)
        }
    }

    private fun setListeners(){
        binding.activityFilteredImageFabShare.setOnClickListener{
            with(Intent(Intent.ACTION_SEND)){
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
        binding.activityFilteredImageIvBack.setOnClickListener{
            onBackPressed()
        }
    }

}