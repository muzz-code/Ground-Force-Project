package com.trapezoidlimited.groundforce.di

import com.trapezoidlimited.groundforce.navigation.AppNavigator
import com.trapezoidlimited.groundforce.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * hilt module for navigation
 */

@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {

    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl):AppNavigator
}