package com.trapezoidlimited.groundforce.repository

import android.app.Activity
import android.widget.ImageView

interface GroundForceRepository {
    fun saveImageFromServer(avatarUrl: String, image: ImageView, activity: Activity)
    fun getImageFromStorage(activity: Activity, imageView: ImageView)
}