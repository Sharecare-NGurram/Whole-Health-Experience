package com.carelon.whe.domain.repository

import com.carelon.whe.domain.model.Medicine

interface MedicationDetailRepository {

    fun updateMedicine(medicine: Medicine?)

}