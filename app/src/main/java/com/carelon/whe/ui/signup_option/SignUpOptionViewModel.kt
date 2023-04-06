package com.carelon.whe.ui.signup_option

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.usecase.MedicationUseCase
import com.carelon.whe.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpOptionViewModel @Inject constructor(
    private val medsUseCase: MedicationUseCase,
    @ApplicationContext var context: Context
): BaseViewModel() {
    val prescriptionCountText = MutableLiveData<String>()
    /**
     * this is for demo delete it in live
     * */
    fun getNewPrescriptionMedicine() {
        viewModelScope.launch {
            val medImports = medsUseCase.getNewPrescriptionAvailable()
            medImports.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        it.data?.let {record->
                            if(record.size==1){
                                prescriptionCountText.value = String.format(
                                    context.getString(R.string.str_we_found_medication),
                                    record.size
                                )
                            }else if(record.size>1){
                                prescriptionCountText.value = String.format(
                                    context.getString(R.string.str_we_found_medications),
                                    record.size
                                )
                            }else{
                                prescriptionCountText.value=""
                            }
                        }
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                }
            }
        }
    }
}