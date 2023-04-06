package com.carelon.whe.util

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.carelon.whe.domain.model.MedicineReminder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.properties.Delegates


/**
 * Request focus to the given view
 */
fun requestFocus(view: View, window: Window){
    if (view.requestFocus()){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}

/**
 * Validate given text field is empty or not
 */
fun validateFieldsNotEmpty() : Boolean {
    return true
}

/**
 * format date in locale
* */
fun getFullDateFormat(date: Date):String{
    return SimpleDateFormat("EEEE, MMMM dd",Locale("en")).format(date)
}

/**
 * used for sorting the medicine reminder
* */
class ComparatorReminder: Comparator<MedicineReminder>{
    override fun compare(medicineReminder1: MedicineReminder, medicineReminder2: MedicineReminder): Int {
        var hrs = if(medicineReminder1.hour==12){
            0
        }else{
            medicineReminder1.hour
        }
        val o1hrs = hrs+(12*medicineReminder1.format)
        val o1sec = (o1hrs*60+medicineReminder1.minutes)*60

        hrs = if(medicineReminder2.hour==12){
            0
        }else{
            medicineReminder2.hour
        }
        val o2hrs = hrs+(12*medicineReminder2.format)
        val o2sec = (o2hrs*60+medicineReminder2.minutes)*60

        return if(o1sec==o2sec){
            0
        }else if(o1sec>o2sec){
            1
        }else{
            -1
        }
    }
}

fun getScreenHeight(activity: Activity): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = activity.windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.top - insets.bottom
    } else {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

fun dateFormatMedicineDetails(dateToFormat: String?) : String? {
    try {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dateFormatExpected = SimpleDateFormat("MMM dd, yyyy")
        val dateObj = dateToFormat?.let { dateFormat.parse(it) }
        dateObj?.let {
            return dateFormatExpected.format(dateObj)
        }
    } catch (e: Exception) {
        return dateToFormat
    }
    return dateToFormat
}

/**
 * live data that will only deliver last record if not already delivered
* */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    var curUser: Boolean by Delegates.vetoable(false) { property, oldValue, newValue ->
        newValue != oldValue
    }

    override fun observe(owner: LifecycleOwner, observer: androidx.lifecycle.Observer<in T>) {
        super.observe(owner, Observer<T> { t ->
            if (curUser) {
                observer.onChanged(t)
                curUser = false
            }
        })
    }

    override fun setValue(t: T?) {

        curUser = true
        super.setValue(t)
    }

    fun call() {
        postValue(null)
    }

}
