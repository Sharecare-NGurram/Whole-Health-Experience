package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.repository.MedicineReminderRepository
import javax.inject.Inject

/**
 * Use Case for deleting medicine reminder from database
 */
class DeleteMedicineReminderUseCase @Inject constructor(
    private val medicineReminderRepository: MedicineReminderRepository) {

    suspend fun deleteMedicineReminder(medicineReminder: MedicineReminder) {
        medicineReminderRepository.deleteMedicineReminder(medicineReminder)
    }
}