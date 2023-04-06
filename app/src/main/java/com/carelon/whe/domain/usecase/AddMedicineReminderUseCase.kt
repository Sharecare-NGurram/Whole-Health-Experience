package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.repository.MedicineReminderRepository
import javax.inject.Inject

/**
 * Use Case for adding medicine reminder in database
 */

class AddMedicineReminderUseCase @Inject constructor(
    private val medicineReminderRepository: MedicineReminderRepository) {

    suspend fun addMedicineReminder(medicineReminder: MedicineReminder) {
        medicineReminderRepository.insertMedicineReminder(medicineReminder)
    }

}