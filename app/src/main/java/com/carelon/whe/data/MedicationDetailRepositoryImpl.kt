package com.carelon.whe.data

import com.apollographql.apollo3.ApolloClient
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.MedicationDetailRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationDetailRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper
) : MedicationDetailRepository {
    /**
     * Update medicine details to the saved medicine list
     */
    override fun updateMedicine(medicine: Medicine?) {
        val savedMedication = preferenceHelper.savedMedication
        val type = object : TypeToken<List<Medicine>>() {}.type
        val medicationsList = Gson().fromJson(savedMedication, type) as ArrayList<Medicine>
        medicine?.let {
            for (pos in 0 until medicationsList.size){
                val medicineFromPrefs = medicationsList[pos]
                if (medicineFromPrefs.id == medicine.id) {
                    medicationsList.set(pos, medicine)
                    break
                }
            }
        }
        preferenceHelper.savedMedication = Gson().toJson(medicationsList)
    }
}