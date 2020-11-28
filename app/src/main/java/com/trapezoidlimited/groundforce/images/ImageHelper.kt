package com.trapezoidlimited.groundforce.images

import android.app.Activity
import android.widget.ImageView

interface ImageHelper {

    fun getImageFromServerAndSave(avatarUrl: String, image: ImageView, activity: Activity)

    fun getImageFromInternalStorage(activity: Activity, imageView: ImageView)

}