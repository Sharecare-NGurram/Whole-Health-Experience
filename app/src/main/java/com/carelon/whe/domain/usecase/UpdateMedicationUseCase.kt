package com.carelon.whe.domain.usecase

import com.carelon.whe.MarkMedicationImportedMutation
import com.carelon.whe.SaveImportMedicationMutation
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface UpdateMedicationUseCase {

    fun saveImportedMedicine(medicines: List<MedicationData>): Flow<Resource<SaveImportMedicationMutation.Data?>>

    fun trackMedicineCompleted(isImported:Boolean): Flow<Resource<MarkMedicationImportedMutation.Data?>>

    fun trackMedicineStatus(): Flow<Resource<Int>>

}