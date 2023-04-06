package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.MedImportAvailability
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface ForYouUseCase {

    suspend fun checkMedImportAvailable() : Flow<Resource<MedImportAvailability?>>

    suspend fun medsImportCanceled(isCancelled: Boolean): Flow<Resource<GeneralResponse?>>

    suspend fun getSavedMedications() : Flow<Resource<List<Medicine?>?>>

    suspend fun removeSavedMeds(): Flow<Resource<GeneralResponse?>>

}