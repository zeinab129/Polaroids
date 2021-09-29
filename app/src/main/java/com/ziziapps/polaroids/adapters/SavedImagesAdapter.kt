package com.ziziapps.polaroids.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziziapps.polaroids.databinding.ItemContainerSavedImageBinding
import com.ziziapps.polaroids.listeners.SavedImageListener
import java.io.File
import java.security.PrivateKey

class SavedImagesAdapter (
    private val savedImages: List<Pair<File, Bitmap>>,
    private val savedImageListener: SavedImageListener
) : RecyclerView.Adapter<SavedImagesAdapter.SavedImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding = ItemContainerSavedImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        with(holder){
            with(savedImages[position]){
                binding.itemContainerSavedImageIvImage.setImageBitmap(second)
                binding.itemContainerSavedImageIvImage.setOnClickListener{
                    savedImageListener.onImageClicked(first)
                }
            }
        }
    }

    override fun getItemCount() = savedImages.size

    inner class SavedImageViewHolder(val binding: ItemContainerSavedImageBinding):
        RecyclerView.ViewHolder(binding.root)
}