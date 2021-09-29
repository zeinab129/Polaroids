package com.ziziapps.polaroids.utilities

import android.content.Context
import android.location.GnssNavigationMessage
import android.view.View
import android.widget.Toast

fun Context.displayToast(message: String?){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.show(){
    this.visibility = View.VISIBLE
}