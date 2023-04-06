package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.Medicine

interface MedicationDetailUseCase {

    suspend fun updateMedicine(medicine: Medicine?)

}