package com.carelon.whe.domain.model

import android.os.Parcelable
import com.carelon.whe.constants.Constants
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DailyCheckIn(
    var hour: Int,

    var minutes: Int,

    var format: Int
) : Parcelable {

    override fun toString(): String {
        val format = if (format == Calendar.AM) {
            Constants.TEXT_AM
        } else {
            Constants.TEXT_PM
        }
        return "${hour}:${String.format("%02d", minutes)} $format"
    }

}
