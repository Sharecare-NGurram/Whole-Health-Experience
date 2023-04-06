package com.carelon.whe.data

import com.carelon.whe.data.data_source.MedicineReminderDao
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.repository.MedicineReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MedicineReminderRepositoryImpl @Inject constructor(
    private val medicineReminderDao: MedicineReminderDao
) : MedicineReminderRepository {

    override suspend fun insertMedicineReminder(medicineReminder: MedicineReminder) {
        medicineReminderDao.insertMedicineReminder(medicineReminder)
    }

    override suspend fun updateMedicineReminder(medicineReminder: MedicineReminder) {
        medicineReminderDao.updateMedicineReminder(medicineReminder)
    }

    override fun getMedicineReminders(): List<MedicineReminder> {
        return medicineReminderDao.getMedicineReminders()
    }

    override suspend fun deleteMedicineReminder(medicineReminder: MedicineReminder) {
        medicineReminderDao.deleteMedicineReminder(medicineReminder)
    }

}