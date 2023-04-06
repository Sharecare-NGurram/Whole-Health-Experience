package com.carelon.whe.data

import android.content.res.AssetManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.MarkMedicationImportedMutation
import com.carelon.whe.MedicationListQuery
import com.carelon.whe.SaveImportMedicationMutation
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.AddMedicationRepository
import com.carelon.whe.type.AddMedicationInput
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddMedicationRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper)
    : AddMedicationRepository {

    override suspend fun getMedicineToImport(): ApolloResponse<MedicationListQuery.Data> {
        return apolloClient.query(
            MedicationListQuery()
        ).execute()
    }

}