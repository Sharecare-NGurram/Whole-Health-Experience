package com.carelon.whe.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.carelon.whe.MedicationListQuery
import com.carelon.whe.base.BaseDataModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Medicine(
    @SerializedName("id")
    val id: String,
    @SerializedName("medName")
    val name: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("doctorName")
    val doctorName: String,
    @SerializedName("prescriptionDate")
    val prescriptionDate: String,
    @SerializedName("pharmacyName")
    val pharmacyName: String,
    @SerializedName("filledDate")
    val filledDate: String,
    @SerializedName("pharmacyAddress")
    val pharmacyAddress: String,
    var notes: String?="",
    var condition: String?="",
    var isSelected: Boolean = false) : BaseDataModel(), Parcelable {
        fun getTruncatedMedicineName() = if (name.length > 96) name.substring(
            0, 96
        ) + "..." else name

        fun getMedicineNameForToast() =  if (name.length > 15) name.substring(
            0, 15
        ) + "..." else name

        object ModelMapper {
            fun from(data: MedicationData) =
                Medicine(
                    type = data.dosageForm,
                    pharmacyName = data.nonProprietaryName,
                    notes = data.notes?:"",
                    name = data.proprietaryName,
                    amount = data.strength,
                    id = data.id,
                    condition = data.conditions?:"",
                    filledDate = "09-07-2022",
                    doctorName = "Dr. Judy Jones",
                    prescriptionDate = "11-06-2022",
                    pharmacyAddress = "976 3rd Ave, Brooklyn, NY 11232, United States",
                )
        }
}
