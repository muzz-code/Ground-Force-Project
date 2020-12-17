package com.trapezoidlimited.groundforce

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.gson.Gson
import com.trapezoidlimited.groundforce.images.ImageHelper
import com.trapezoidlimited.groundforce.images.ImageHelperImpl
import com.trapezoidlimited.groundforce.repository.GroundForceRepository
import com.trapezoidlimited.groundforce.repository.GroundForceRepositoryImpl
import com.trapezoidlimited.groundforce.room.DataBase
import com.trapezoidlimited.groundforce.room.RoomRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomViewModel
import com.trapezoidlimited.groundforce.room.RoomViewModelFactory
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import javax.inject.Inject

@HiltAndroidApp
class EntryApplication : Application() {
    @Inject
    lateinit var retrofit: Retrofit

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

        fun roomViewModel(fragment: Fragment): RoomViewModel {
            return ViewModelProvider(fragment, roomViewModelFactory).get(
                RoomViewModel::class.java
            )
        }

        fun viewModel(viewModelStoreOwner: ViewModelStoreOwner): RoomViewModel {
            return ViewModelProvider(viewModelStoreOwner, roomViewModelFactory).get(
                RoomViewModel::class.java
            )
        }

        fun applicationContext(): Context {
            return instance.applicationContext
        }

//        fun authViewModel(fragment: Fragment): AuthViewModel {
//            val repository = AuthRepositoryImpl(loginApiService)
//            val factory = ViewModelFactory(repository)
//            return ViewModelProvider(fragment, factory).get(AuthViewModel::class.java)
//        }

        val gson by lazy { Gson() }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}