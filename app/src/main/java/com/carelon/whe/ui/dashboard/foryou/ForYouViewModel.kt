package com.carelon.whe.ui.dashboard.foryou

import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.MedImportAvailability
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.model.UserVerificationData
import com.carelon.whe.domain.usecase.ForYouUseCase
import com.carelon.whe.domain.usecase.UserProfileUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.SingleLiveEvent
import com.carelon.whe.util.getFullDateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val forYouUseCase: ForYouUseCase,
    private val userProfileUseCase: UserProfileUseCase,
    @ApplicationContext var context: Context
): BaseViewModel() {

    val userName = SingleLiveEvent<String>()
    val forYouStatus= SingleLiveEvent<UserVerificationData>()

    private var selectedDate : Date?=null
    fun setSelectedDate(value:Date){
        selectedDate = value
    }

    fun getShowingDate():String{
        return if(selectedDate!=null) {
            String.format(
                context.getString(R.string.str_for_date),
                getFullDateFormat(date = selectedDate!!)
            )
        }else{
            String.format(
                context.getString(R.string.str_for_date),
                getFullDateFormat(date = Calendar.getInstance().time)
            )
        }
    }

    fun getUserData(){
        viewModelScope.launch {
            userProfileUseCase.getUserData().collect {response->
                when(response){
                    is Resource.Loading -> {
                        setIsLoading(true)
                    }
                    is Resource.Success -> {
                        setIsLoading(false)
                        response.data?.let {dt->
                            forYouStatus.value=dt
                            userName.value = dt.userName
                        }
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

}