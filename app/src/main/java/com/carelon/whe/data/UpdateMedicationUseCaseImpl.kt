package com.carelon.whe.data.data_source

import com.carelon.whe.MarkMedicationImportedMutation
import com.carelon.whe.SaveImportMedicationMutation
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.domain.repository.AddMedicationRepository
import com.carelon.whe.domain.repository.UpdateMedicationRepository
import com.carelon.whe.domain.usecase.UpdateMedicationUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.type.AddMedicationInput
import com.carelon.whe.type.ImportAction
import com.carelon.whe.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateMedicationUseCaseImpl @Inject constructor(
    private val updateMedsRepository: UpdateMedicationRepository,
): UpdateMedicationUseCase {

    override fun saveImportedMedicine(medicines: List<MedicationData>): Flow<Resource<SaveImportMedicationMutation.Data?>> =
        flow {
            emit(Resource.Loading)
            try {
                val inputData = medicines.map { AddMedicationInput(
                    dosageForm = it.dosageForm,
                    ndcPackageCode = it.ndcPackageCode,
                    nonProprietaryName = it.nonProprietaryName,
                    proprietaryName = it.proprietaryName,
                    route = it.route,
                    strength = it.strength,
                ) }
                val response = updateMedsRepository.saveImportedMedicine(inputData)
                emit(Resource.Success(data = response.data))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun trackMedicineCompleted(isImported:Boolean): Flow<Resource<MarkMedicationImportedMutation.Data?>> =
        flow {
            emit(Resource.Loading)
            try {
                val input = mutableListOf<ImportAction>()
                if(isImported)
                    input.add(ImportAction.OK)
                else
                    input.add(ImportAction.NOT_RIGHT_NOW)
                val response = updateMedsRepository.markImportMedicineFinished(input)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun trackMedicineStatus(): Flow<Resource<Int>> =
        flow {
            emit(Resource.Loading)
            try {
                val response = updateMedsRepository.trackMedicineStatus()
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)
}