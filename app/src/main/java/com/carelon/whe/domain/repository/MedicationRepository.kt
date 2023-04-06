package com.carelon.whe.domain.repository

import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.Medicine

interface MedicationRepository {

    fun saveSelectedMedication(selectedMeds:String?): GeneralResponse?

    fun getSavedMedication() : ArrayList<Medicine>?

    fun getNewPrescription() : ArrayList<Medicine>?

    fun markedNewPrescriptionRead()

}