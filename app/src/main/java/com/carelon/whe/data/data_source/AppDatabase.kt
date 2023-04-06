package com.carelon.whe.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carelon.whe.domain.model.MedicineReminder

@Database(
    entities = [MedicineReminder::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val medicineReminderDao: MedicineReminderDao

    companion object {
        const val DATABASE_NAME = "whe_db"
    }
}

object TableConstant {
    const val MEDICINE_REMINDER_ENTITY = "medicineReminder"
}