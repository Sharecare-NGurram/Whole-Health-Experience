package com.carelon.whe.data

import android.content.res.AssetManager
import com.apollographql.apollo3.ApolloClient
import com.carelon.whe.data.data_source.MedicineReminderDao
import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.MedicationRepository
import com.carelon.whe.domain.repository.MedicineReminderRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper
)
    : MedicationRepository {

    @Inject
    lateinit var assetManager : AssetManager

    override fun getSavedMedication(): ArrayList<Medicine> {
        val savedMedication = preferenceHelper.savedMedication
        val type = object : TypeToken<List<Medicine>>() {}.type
        return Gson().fromJson(savedMedication, type) as ArrayList<Medicine>
    }

    override fun getNewPrescription(): ArrayList<Medicine>? {
        var arrayList = arrayListOf<Medicine>()
        try {
            val inputStream = assetManager.open("new_prescription_mock.json")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charset.forName("UTF-8"))
            val responseObj = JSONObject(jsonString)
            if(!preferenceHelper.isPrescriptionRead) {
                val type = object : TypeToken<List<Medicine>>() {}.type
                arrayList = Gson().fromJson(responseObj.getJSONArray("medicines").toString(), type) as ArrayList<Medicine>
            }
        } catch (e: Exception){
            return null
        }

        return arrayList
    }

    override fun markedNewPrescriptionRead() {
        preferenceHelper.isPrescriptionRead = !preferenceHelper.isPrescriptionRead
    }

    override fun saveSelectedMedication(selectedMeds: String?): GeneralResponse {
        preferenceHelper.savedMedication = selectedMeds
        return GeneralResponse(success = true)
    }
}