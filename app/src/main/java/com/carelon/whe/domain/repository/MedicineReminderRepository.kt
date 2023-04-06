package com.carelon.whe.domain.repository

import com.carelon.whe.domain.model.MedicineReminder
import kotlinx.coroutines.flow.Flow

interface MedicineReminderRepository {

    suspend fun insertMedicineReminder(medicineReminder: MedicineReminder)

    suspend fun updateMedicineReminder(medicineReminder: MedicineReminder)

    fun getMedicineReminders(): List<MedicineReminder>

    suspend fun deleteMedicineReminder(medicineReminder: MedicineReminder)

}