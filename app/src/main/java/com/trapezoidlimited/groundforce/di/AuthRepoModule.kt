package com.trapezoidlimited.groundforce.di

import com.trapezoidlimited.groundforce.repository.AuthRepository
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


/**
 * providing the repository for injection */

@InstallIn(ApplicationComponent::class)
@Module
abstract class AuthRepoModule {
//    @Binds
//    abstract fun bindAuthRepo(loginImpl: AuthRepositoryImpl):AuthRepository
}