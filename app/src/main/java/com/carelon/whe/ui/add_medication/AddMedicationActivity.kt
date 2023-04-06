package com.carelon.whe.ui.add_medication

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.base.BaseViewHolder
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.ActivityAddMedicationBinding
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.ui.add_medication.adapter.MedicationsListAdapter
import com.carelon.whe.util.setSafeOnClickListener
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity class for medicine import
 */
@AndroidEntryPoint
class AddMedicationActivity : BaseActivity<ActivityAddMedicationBinding, AddMedicationViewModel>() {

    private val addMedicationViewModel by viewModels<AddMedicationViewModel>()

    override fun getContentView(): Int = R.layout.activity_add_medication

    override fun getBindingVariable(): Int = BR.vm

    override fun getViewModel(): AddMedicationViewModel = addMedicationViewModel

    override fun showLightStatusBar(): Boolean = true

    @Inject
    lateinit var adapter: MedicationsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(BundleKeyConstants.EXTRA_ADD_MEDICATION_TRIGGERED_FROM)) {
            addMedicationViewModel.updateIsFromMeds(intent.getStringExtra(BundleKeyConstants.EXTRA_ADD_MEDICATION_TRIGGERED_FROM))
        }
        addMedicationViewModel.updateSubmitButtonText()
        addMedicationViewModel.fetchMedicine()
        observeData()
        initListeners()
    }

    private fun initListeners() {
        getDataBinding()?.imgClose?.setSafeOnClickListener { finish() }
        getDataBinding()?.btnSubmit?.setSafeOnClickListener {
            if(addMedicationViewModel.selectedMedicines.isEmpty()){
                medsImported()
            }else{
                addMedicationViewModel.saveMedicine()
            }
        }
    }

    // Return selected medicine size to previous requested screen
    private fun medsImported() {
        val resultIntent = Intent()
        resultIntent.putExtra(
            BundleKeyConstants.EXTRA_IMPORT_MEDICINES_COUNT,
            addMedicationViewModel.selectedMedicines.size
        )
        resultIntent.putExtra(
            BundleKeyConstants.EXTRA_IMPORT_MEDICINES,
            Gson().toJson(addMedicationViewModel.selectedMedicines)
        )
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    /**
     * Observe medicine data to update the UI
     */
    private fun observeData() {
        addMedicationViewModel.medicinesLD.observe(this) {
            getDataBinding()?.recyclerMedicines?.adapter = adapter
            if (it.isEmpty()) {
                addMedicationViewModel.setIsFailedToFetch(true)
                getDataBinding()?.imgFailedToFetch?.setImageResource(R.drawable.ic_medicine)
                getDataBinding()?.txtFailedToFetchHeader?.text =
                    getString(R.string.no_medications_found)
                getDataBinding()?.txtFailedToFetchInfo?.text =
                    getString(R.string.no_medications_found_info)
            } else {
                /**
                 * Listener for list item click, check the medicine is selected already and apply selection/un-selection
                 */
                adapter.clickListenerWithPosition = object :
                    BaseViewHolder.ItemClickedCallBackWithPosition<MedicationData?> {
                    override fun onItemClicked(data: MedicationData?, pos: Int) {
                        data?.let {
                            data.isSelected = !data.isSelected
                            adapter.updateDataWithPos(data, pos)
                            addMedicationViewModel.updateSelectedItems(data)
                        }
                    }
                }
                adapter.addAll(it.filterNotNull())
            }
        }
        addMedicationViewModel.isNoMedicinesSelected.observe(this) {
            if (it){
                getDataBinding()?.btnSubmit?.isEnabled = false
                getDataBinding()?.btnSubmit?.backgroundTintList =
                    ContextCompat.getColorStateList(this@AddMedicationActivity, R.color.light_gray_add_medication)
                getDataBinding()?.btnSubmit?.setTextColor(getColor(R.color.medium_gray))
            }else{
                getDataBinding()?.btnSubmit?.isEnabled = true
                getDataBinding()?.btnSubmit?.backgroundTintList =
                    ContextCompat.getColorStateList(this@AddMedicationActivity, R.color.button_purple)
                getDataBinding()?.btnSubmit?.setTextColor(getColor(R.color.text_colorSecondary))
            }
        }
        addMedicationViewModel.savedMedicinesLD.observe(this){
            medsImported()
        }
    }
}