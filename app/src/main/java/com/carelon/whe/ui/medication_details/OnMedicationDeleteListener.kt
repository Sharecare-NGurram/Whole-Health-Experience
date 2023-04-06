package com.carelon.whe.ui.medication_details

import com.carelon.whe.domain.model.Medicine

interface OnMedicationDeleteListener {

    fun onMedicationDeleted(medicine: Medicine?)

}