package com.trapezoidlimited.groundforce.repository

import android.app.Activity
import android.widget.ImageView
import com.trapezoidlimited.groundforce.images.ImageHelper

class GroundForceRepositoryImpl(
    private val imageHelper: ImageHelper
) : GroundForceRepository {

    override fun saveImageFromServer(avatarUrl: String, image: ImageView, activity: Activity) {
        imageHelper.getImageFromServerAndSave(avatarUrl, image, activity)
    }

    override fun getImageFromStorage(activity: Activity, imageView: ImageView) {
        imageHelper.getImageFromInternalStorage(activity, imageView)
    }

}