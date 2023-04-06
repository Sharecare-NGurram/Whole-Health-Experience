package com.carelon.whe.ui.medication_details

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseBottomSheetFragment
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.FragmentMedicationDetailsBottomSheetBinding
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.util.getScreenHeight
import com.carelon.whe.util.parcelable
import com.carelon.whe.util.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MedicationDetailsFragment :
    BaseBottomSheetFragment<FragmentMedicationDetailsBottomSheetBinding>() {

    private val medicationDetailViewModel by viewModels<MedicationDetailsViewModel>()

    override fun getContentView(): Int = R.layout.fragment_medication_details_bottom_sheet

    override fun getBindingVariable(): Int = BR.vm

    override fun getViewModel(): BaseViewModel = medicationDetailViewModel

    private var listener: OnMedicationDeleteListener? = null

    private val parcelableMedicineData: Medicine? by lazy {
        requireArguments().parcelable(BundleKeyConstants.MEDICINES_TO_DISPLAY_IN_DETAILS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        medicationDetailViewModel.setMedicineToDisplay(parcelableMedicineData)
        setListeners()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            (this as BottomSheetDialog).behavior.peekHeight = getScreenHeight(requireActivity()) - 150
            this.behavior.addBottomSheetCallback(object: BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    Log.d("TAG","onStateChanged")
                    if (newState == 4) {
                        medicationDetailViewModel.saveMedicineDetails()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    Log.d("TAG","onSlide")
                }
            })
        }
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { dialogInterface, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                medicationDetailViewModel.saveMedicineDetails()
            }
            false
        }
    }

    private fun setListeners() {
        getDataBinding()?.txtDelete?.setSafeOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.remove_medication_title))
            builder.setMessage(
                String.format(
                    getString(R.string.remove_medication_message),
                    parcelableMedicineData?.getTruncatedMedicineName()
                )
            )
            builder.setPositiveButton(getString(R.string.remove)) { dialogInterface, i ->
                listener?.onMedicationDeleted(medicine = parcelableMedicineData)
                dialogInterface.cancel()
                dialog?.cancel()
            }
            builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
                dialogInterface.cancel()
            }
            builder.setCancelable(isCancelable)
            builder.create().show()
        }

        getDataBinding()?.imgClose?.setSafeOnClickListener {
            medicationDetailViewModel.saveMedicineDetails()
            dialog?.cancel()
        }

        getDataBinding()?.txtViewOnMaps?.setSafeOnClickListener {
            try {
                val gmmIntentUri =
                    Uri.parse("geo:0,0?q=${parcelableMedicineData?.pharmacyAddress}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }
        }
    }

    fun setMedicationDeleteListener(medicationDeleteListener: OnMedicationDeleteListener) {
        listener = medicationDeleteListener
    }

}