package com.carelon.whe.domain.model

data class FocusAreaItem(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    var isCompleted: Boolean = false,
    var shouldShowDenied: Boolean = false,
    val errorDescription: String? = null,
)
