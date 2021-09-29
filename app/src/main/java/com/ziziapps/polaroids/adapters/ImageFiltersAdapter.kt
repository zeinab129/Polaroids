package com.ziziapps.polaroids.adapters

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ziziapps.polaroids.R
import com.ziziapps.polaroids.data.ImageFilter
import com.ziziapps.polaroids.databinding.ItemContainerFilterBinding
import com.ziziapps.polaroids.listeners.ImageFilterListener

class ImageFiltersAdapter (
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
    ): RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>(){

    private var selectedFilterPosition = 0
    private var previouslySelectedPosition = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.itemContainerFilterImImageFilterPreview.setImageBitmap(filterPreview)
                binding.itemContainerFilterTvName.text = name
                binding.root.setOnClickListener{
                    if(position != selectedFilterPosition){
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedPosition = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFiltersAdapter){
                            notifyItemChanged(previouslySelectedPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }
                }
                binding.itemContainerFilterTvName.setTextColor(
                    ContextCompat.getColor(
                        binding.itemContainerFilterTvName.context,
                        if(selectedFilterPosition == position)
                            R.color.white
                        else
                            R.color.green
                    )
                )
                binding.itemContainerFilterTvName.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        binding.itemContainerFilterTvName.context,
                        if(selectedFilterPosition == position)
                            R.drawable.shape_item_container_selected_filter_name
                        else
                            R.drawable.shape_item_container_unselected_filter_name
                    )
                )
                binding.itemContainerFilterImImageFilterPreview.setBorderColor(
                    ContextCompat.getColor(
                        binding.itemContainerFilterTvName.context,
                        if(selectedFilterPosition == position)
                            R.color.green
                        else
                            R.color.transparent
                    )
                )
            }
        }
    }

    override fun getItemCount() = imageFilters.size



    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding) :
        RecyclerView.ViewHolder(binding.root)


}