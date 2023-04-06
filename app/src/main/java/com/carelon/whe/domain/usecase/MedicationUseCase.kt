package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface MedicationUseCase {

    suspend fun saveSelectedMeds(selectedMeds: String?): Flow<Resource<GeneralResponse?>>

    suspend fun getSavedMedications() : Flow<Resource<List<Medicine?>?>>

    suspend fun getNewPrescriptionAvailable() : Flow<Resource<List<Medicine?>?>>

    fun markedNewPrescriptionRead()

}