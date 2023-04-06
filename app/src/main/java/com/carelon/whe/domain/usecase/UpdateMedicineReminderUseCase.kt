package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.repository.MedicineReminderRepository
import javax.inject.Inject

/**
 * Use Case for updating medicine reminder in database
 */
class UpdateMedicineReminderUseCase @Inject constructor(
    private val medicineReminderRepository: MedicineReminderRepository) {

    suspend fun updateMedicineReminder(medicineReminder: MedicineReminder) {
        medicineReminderRepository.updateMedicineReminder(medicineReminder)
    }
}