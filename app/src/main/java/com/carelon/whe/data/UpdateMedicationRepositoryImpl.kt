package com.carelon.whe.data

import android.content.res.AssetManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.carelon.whe.MarkMedicationImportedMutation
import com.carelon.whe.MedicationListQuery
import com.carelon.whe.SaveImportMedicationMutation
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.AddMedicationRepository
import com.carelon.whe.domain.repository.UpdateMedicationRepository
import com.carelon.whe.type.AddMedicationInput
import com.carelon.whe.type.ImportAction
import com.carelon.whe.util.TackMedicationStatus
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateMedicationRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper)
    : UpdateMedicationRepository {

    override suspend fun saveImportedMedicine(medicines: List<AddMedicationInput>):
            ApolloResponse<SaveImportMedicationMutation.Data> {
        return apolloClient.mutation(
            SaveImportMedicationMutation(medicines)
        ).execute()
    }

    override suspend fun markImportMedicineFinished(input : List<ImportAction>): ApolloResponse<MarkMedicationImportedMutation.Data> {
        if(input.first().rawValue=="OK")
            preferenceHelper.isTrackMedsCompleted= TackMedicationStatus.MEDICATION_IMPORT_OK.value
        else
            preferenceHelper.isTrackMedsCompleted= TackMedicationStatus.MEDICATION_IMPORT_NOT_NOW.value

        return apolloClient.mutation(
            MarkMedicationImportedMutation(Optional.present(input))
        ).execute()
    }

    override suspend fun trackMedicineStatus(): Int {
        return preferenceHelper.isTrackMedsCompleted
    }

}