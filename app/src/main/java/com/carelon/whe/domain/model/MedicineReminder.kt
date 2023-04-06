package com.carelon.whe.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carelon.whe.constants.Constants
import com.carelon.whe.data.data_source.TableConstant
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = TableConstant.MEDICINE_REMINDER_ENTITY)
@Parcelize
data class MedicineReminder(

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,

    @ColumnInfo(name = "hour")
    var hour: Int,

    @ColumnInfo(name = "minutes")
    var minutes: Int,

    @ColumnInfo(name = "format")
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