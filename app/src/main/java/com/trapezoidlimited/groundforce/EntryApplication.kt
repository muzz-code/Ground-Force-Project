package com.trapezoidlimited.groundforce

import android.app.Application
import com.trapezoidlimited.groundforce.files.FilesHelper
import com.trapezoidlimited.groundforce.files.FilesHelperImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EntryApplication : Application() {

    companion object {
        private lateinit var instance: EntryApplication


        private val filesHelper: FilesHelper by lazy {
            FilesHelperImpl(instance.filesDir)
        }
    }

}