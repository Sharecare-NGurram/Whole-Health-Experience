package com.carelon.whe.domain.usecase

import com.carelon.whe.MarkMedicationImportedMutation
import com.carelon.whe.MedicationListQuery
import com.carelon.whe.SaveImportMedicationMutation
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface AddMedicationUseCase {

    fun getMedicineList() : Flow<Resource<MedicationListQuery.Data?>>

}