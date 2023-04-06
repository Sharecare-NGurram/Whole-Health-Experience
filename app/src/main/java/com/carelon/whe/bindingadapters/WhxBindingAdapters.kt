package com.carelon.whe.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.carelon.whe.R
import com.carelon.whe.constants.Constants
import com.carelon.whe.domain.model.AppFeatureModel
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.ui.dashboard.foryou.calender.CurrentDayRecord
import com.carelon.whe.util.dateFormatMedicineDetails
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:visibility")
fun View.bindVisibility(visible: Boolean?) {
    isVisible = visible == true
}

@BindingAdapter("app:medicine")
fun View.bindMedicine(medicine: MedicationData) {
    val parent = this as ConstraintLayout
    val child = parent.findViewById<ConstraintLayout>(R.id.constraint_child)
    val icon = child.findViewById<ImageView>(R.id.img_medicine)
    val label = child.findViewById<TextView>(R.id.txt_medicine_name)
    val colorToApply = if (medicine.isSelected) ContextCompat.getColor(
        context,
        R.color.medication_selected
    ) else ContextCompat.getColor(context, R.color.purple)
    val colorToApplyBG = if (medicine.isSelected) ContextCompat.getDrawable(
        context,
        R.drawable.bg_medicine_selected_inner
    ) else null
    val bgToApply = if (medicine.isSelected) ContextCompat.getDrawable(
        context,
        R.drawable.bg_medicine_selected
    ) else ContextCompat.getDrawable(context, R.drawable.bg_medicine_unselected)
    icon.setColorFilter(colorToApply, android.graphics.PorterDuff.Mode.SRC_IN)
    label.setTextColor(colorToApply)
    label.text = medicine.proprietaryName
    parent.background = bgToApply
    child.background = colorToApplyBG
}

@BindingAdapter("app:currentDayRecord")
fun View.bindCalendar(itemData: CurrentDayRecord) {
    itemData.let {
        val parent = this as ConstraintLayout
        val progressBar = parent.findViewById<ProgressBar>(R.id.progressBar)
        val txtDay = parent.findViewById<TextView>(R.id.txt_day)
        val txtDayOfWeek = parent.findViewById<TextView>(R.id.txt_day_of_week)

        val selectedTime = Calendar.getInstance()
        selectedTime.time = itemData.date

        /*if (itemData.enabled == 1) {
            binding.progressBar.progress = 100
        } else {
            binding.progressBar.progress = 0
        }*/

        txtDayOfWeek.text =
            "${SimpleDateFormat("EEEE", Locale.ENGLISH).format(selectedTime.time)[0]}"
        txtDay.text = "${selectedTime.get(Calendar.DAY_OF_MONTH)}"
    }
}

@BindingAdapter("app:medicineReminderTime")
fun TextView.bindMedicineReminderTime(medicineReminder: MedicineReminder) {
    text = medicineReminder.toString()
}

@BindingAdapter("app:medicineType")
fun TextView.bindMedicineType(medicine: Medicine?) {
    text = medicine?.amount + " " + medicine?.type
}

@BindingAdapter("app:doctorDetails")
fun TextView.bindDoctorDetails(medicine: Medicine?) {
    text = medicine?.doctorName + " on " + dateFormatMedicineDetails(medicine?.prescriptionDate)
}

@BindingAdapter("app:pharmacyDetails")
fun TextView.bindPharmacyDetails(medicine: Medicine?) {
    text = medicine?.pharmacyName + " on " + dateFormatMedicineDetails(medicine?.filledDate) + "\n" + medicine?.pharmacyAddress
}

/**
 * based on @id we are fetching drawable from local and setting in the background of description
* */
@BindingAdapter("app:featuresData")
fun View.bindAppFeatures(data: AppFeatureModel?) {
    data?.let {
        val parent = this as ConstraintLayout
        val resId = when(it.id){
            1 -> R.drawable.feature_first
            2 -> R.drawable.feature_second
            3 -> R.drawable.feature_third
            4 -> R.drawable.feature_fourth
            else->R.drawable.feature_first
        }
        parent.findViewById<ImageView>(R.id.img_background).setImageResource(resId)
        parent.findViewById<TextView>(R.id.text_index).text="${it.id}"
        parent.findViewById<TextView>(R.id.text_description).text=it.description
    }
}