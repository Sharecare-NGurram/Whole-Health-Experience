package com.carelon.whe.domain.repository

import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.Medicine

interface ForYouRepository {

    fun getMedicine() : String?

    fun medsImportCanceled() : GeneralResponse?

    fun getSavedMedication() : ArrayList<Medicine>?

    fun removeSavedMedication()

}