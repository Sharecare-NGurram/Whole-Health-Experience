package com.carelon.whe.data.data_source

import androidx.room.*
import com.carelon.whe.domain.model.MedicineReminder
/**
 * Dao class for executing queries in medicine reminder table
 */
@Dao
interface MedicineReminderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMedicineReminder(medicineReminder: MedicineReminder)

    @Update
    suspend fun updateMedicineReminder(medicineReminder: MedicineReminder)

    @Query("SELECT * FROM ${TableConstant.MEDICINE_REMINDER_ENTITY}")
    fun getMedicineReminders(): List<MedicineReminder>

    @Delete
    suspend fun deleteMedicineReminder(medicineReminder: MedicineReminder)
}