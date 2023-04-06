package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.MarkMedicationImportedMutation
import com.carelon.whe.MedicationListQuery
import com.carelon.whe.SaveImportMedicationMutation
import com.carelon.whe.type.AddMedicationInput
import com.carelon.whe.type.ImportAction

interface UpdateMedicationRepository {
    suspend fun saveImportedMedicine(medicines: List<AddMedicationInput>) : ApolloResponse<SaveImportMedicationMutation.Data>

    suspend fun markImportMedicineFinished(input : List<ImportAction>) : ApolloResponse<MarkMedicationImportedMutation.Data>

    suspend fun trackMedicineStatus() : Int
}