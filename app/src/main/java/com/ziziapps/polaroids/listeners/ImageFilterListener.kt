package com.ziziapps.polaroids.listeners

import com.ziziapps.polaroids.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}