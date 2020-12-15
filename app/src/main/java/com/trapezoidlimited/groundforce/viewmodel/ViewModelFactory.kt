package com.trapezoidlimited.groundforce.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.repository.BaseRepository
import com.trapezoidlimited.groundforce.room.RoomRepository
import com.trapezoidlimited.groundforce.room.RoomRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomViewModel


class ViewModelFactory(
    private val repository: BaseRepository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            /**
             * When adding a new view model class, create another instance with
             * modelClass.isAssignableFrom(NewViewModel::class.java)
             */
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repository as AuthRepositoryImpl, context) as T

            modelClass.isAssignableFrom(MissionsViewModel::class.java) ->
                MissionsViewModel(repository as AuthRepositoryImpl, context) as T

            else -> throw IllegalArgumentException("View Model Class Not found")
        }
    }
}


