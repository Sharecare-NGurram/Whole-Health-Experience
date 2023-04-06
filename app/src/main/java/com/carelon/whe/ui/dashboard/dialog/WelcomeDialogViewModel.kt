package com.carelon.whe.ui.dashboard.dialog

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class WelcomeDialogViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : BaseViewModel() {

    val welcomeText = MutableLiveData<String>()

    fun setWelcomeUserName(username: String) {
        val welcomeUserName = if (username.isNotEmpty()) {
            "${context.getString(R.string.str_welcome)}, $username"
        } else {
            context.getString(R.string.str_welcome)
        }

        welcomeText.value = welcomeUserName
    }
}