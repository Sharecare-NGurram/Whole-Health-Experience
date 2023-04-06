package com.carelon.whe.di

import android.content.Context
import android.content.res.AssetManager
import com.carelon.whe.ui.medicine_reminder.scheduler.AlarmScheduler
import com.carelon.whe.ui.medicine_reminder.scheduler.AlarmSchedulerImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.HistoryClient
import com.google.android.gms.fitness.data.DataType
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AlarmSchedulerImpl(context)
    }

    @Provides
    @Singleton
    fun provideFitnessOptions(): FitnessOptions {
        return FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInAccount(
        @ApplicationContext context: Context,
        fitnessOptions: FitnessOptions
    ): GoogleSignInAccount {
        return GoogleSignIn.getAccountForExtension(context, fitnessOptions)
    }

    @Provides
    @Singleton
    fun provideFitnessHistoryClient(
        @ApplicationContext context: Context,
        googleSignInAccount: GoogleSignInAccount
    ): HistoryClient {
        return Fitness.getHistoryClient(context, googleSignInAccount)
    }
}