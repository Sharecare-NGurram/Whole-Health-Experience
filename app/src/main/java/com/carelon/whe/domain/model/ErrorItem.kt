package com.carelon.whe.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorItem(
    val title: String? = null,
    val message: String? = null,
    val errorCode: Int? = null
) : Parcelable
