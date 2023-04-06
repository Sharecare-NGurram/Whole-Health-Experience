package com.carelon.whe.data

import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.repository.MedicationDetailRepository
import com.carelon.whe.domain.usecase.MedicationDetailUseCase
import javax.inject.Inject

class MedicationDetailUseCaseImpl @Inject constructor(
    val repository: MedicationDetailRepository
) : MedicationDetailUseCase {
    override suspend fun updateMedicine(medicine: Medicine?) {
        repository.updateMedicine(medicine)
    }
}