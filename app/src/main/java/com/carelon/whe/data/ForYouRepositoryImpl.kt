package com.carelon.whe.data

import android.content.res.AssetManager
import com.apollographql.apollo3.ApolloClient
import com.carelon.whe.constants.PreferenceKeyConstants
import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.ForYouRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForYouRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper)
    : ForYouRepository {

    @Inject
    lateinit var assetManager : AssetManager

    //get the medicines for the user in there prescription
    override fun getMedicine(): String? {
        var jsonString = ""
        try {
            val inputStream = assetManager.open("med_mock.json")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            jsonString = String(buffer, Charset.forName("UTF-8"))
        } catch (e: Exception){
            return null
        }
        return jsonString
    }

    //saves user action of not importing meds for now
    override fun medsImportCanceled(): GeneralResponse {
        return GeneralResponse(success = true)
    }

    override fun getSavedMedication(): ArrayList<Medicine> {
        val savedMedication = preferenceHelper.savedMedication
        val type = object : TypeToken<List<Medicine>>() {}.type
        return Gson().fromJson(savedMedication, type) as ArrayList<Medicine>
    }

    override fun removeSavedMedication(){
        preferenceHelper.removeKey(PreferenceKeyConstants.SAVED_MEDICATION)
    }
}