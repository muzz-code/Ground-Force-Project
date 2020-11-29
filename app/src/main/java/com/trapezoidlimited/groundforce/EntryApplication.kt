package com.trapezoidlimited.groundforce

import android.app.Application
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.trapezoidlimited.groundforce.images.ImageHelper
import com.trapezoidlimited.groundforce.images.ImageHelperImpl
import com.trapezoidlimited.groundforce.repository.GroundForceRepository
import com.trapezoidlimited.groundforce.repository.GroundForceRepositoryImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EntryApplication : Application() {

    companion object {
        private lateinit var instance: EntryApplication

        private val filesHelper: ImageHelper by lazy {
            ImageHelperImpl()
        }

        val groundForceRepository: GroundForceRepository by lazy {
            GroundForceRepositoryImpl(filesHelper)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}