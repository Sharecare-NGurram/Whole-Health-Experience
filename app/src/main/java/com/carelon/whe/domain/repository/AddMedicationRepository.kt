package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.MarkMedicationImportedMutation
import com.carelon.whe.MedicationListQuery
import com.carelon.whe.SaveImportMedicationMutation
import com.carelon.whe.type.AddMedicationInput

interface AddMedicationRepository {
    suspend fun getMedicineToImport() : ApolloResponse<MedicationListQuery.Data>

}