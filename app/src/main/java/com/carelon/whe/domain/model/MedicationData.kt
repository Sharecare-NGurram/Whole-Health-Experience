package com.carelon.whe.domain.model

import android.os.Parcelable
import com.carelon.whe.MedicationListQuery
import com.carelon.whe.base.BaseDataModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MedicationData(
    val conditions: String?,
    val dosageForm: String,
    val ndcPackageCode: String,
    val nonProprietaryName: String,
    val notes: String?,
    val proprietaryName: String,
    val route: String,
    val strength: String,
    val id: String,
    var isSelected: Boolean = false
) : BaseDataModel(),Parcelable{
    fun getTruncatedMedicineName() = if (proprietaryName.length > 96) proprietaryName.substring(
        0, 96
    ) + "..." else proprietaryName

    fun getMedicineNameForToast() =  if (proprietaryName.length > 15) proprietaryName.substring(
        0, 15
    ) + "..." else proprietaryName

    object ModelMapper {
        fun from(data: MedicationListQuery.DispensedMedication) =
            MedicationData(
                conditions = "",
                dosageForm = data.details!!.dosageForm,
                ndcPackageCode = data.ndcPackageCode?:"",
                nonProprietaryName = data.details.nonProprietaryName,
                notes = "",
                proprietaryName = data.details.proprietaryName,
                route = data.details.route,
                strength = data.details.strength,
                id = ""
            )
    }
}
