package com.ziziapps.polaroids.listeners

import java.io.File

interface SavedImageListener {
    fun onImageClicked(file: File)
}