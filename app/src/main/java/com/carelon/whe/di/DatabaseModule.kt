package com.carelon.whe.di

import android.app.Application
import androidx.room.Room
import com.carelon.whe.data.data_source.MedicineReminderDao
import com.carelon.whe.data.data_source.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideMedicineReminderDao(appDatabase: AppDatabase): MedicineReminderDao {
        return appDatabase.medicineReminderDao
    }

}