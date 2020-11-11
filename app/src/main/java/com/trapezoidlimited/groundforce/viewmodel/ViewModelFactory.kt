package com.trapezoidlimited.groundforce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trapezoidlimited.groundforce.repository.AuthRepository
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.repository.BaseRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            /**
             * When adding a new view model class, create another instance with
             * modelClass.isAssignableFrom(NewViewModel::class.java)
             */
            modelClass.isAssignableFrom(LoginAuthViewModel::class.java) ->
                LoginAuthViewModel(repository as AuthRepositoryImpl) as T
            else -> throw IllegalArgumentException("View Model Class Not found")
        }
    }
}