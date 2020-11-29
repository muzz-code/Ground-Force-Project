package com.trapezoidlimited.groundforce

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.trapezoidlimited.groundforce.images.ImageHelper
import com.trapezoidlimited.groundforce.images.ImageHelperImpl
import com.trapezoidlimited.groundforce.repository.GroundForceRepository
import com.trapezoidlimited.groundforce.repository.GroundForceRepositoryImpl
import com.trapezoidlimited.groundforce.room.DataBase
import com.trapezoidlimited.groundforce.room.RoomRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomViewModel
import com.trapezoidlimited.groundforce.room.RoomViewModelFactory
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

        private val roomDao by lazy { DataBase.getDatabase(instance).dao() }
        private val roomRepository by lazy { RoomRepositoryImpl(roomDao) }
        private val roomViewModelFactory by lazy { RoomViewModelFactory(roomRepository) }

        fun viewModel(fragment: Fragment): RoomViewModel {
            return ViewModelProvider(fragment, roomViewModelFactory).get(
                RoomViewModel::class.java
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}