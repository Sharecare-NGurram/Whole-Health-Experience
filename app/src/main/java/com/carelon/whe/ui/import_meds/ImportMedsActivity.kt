package com.carelon.whe.ui.import_meds

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.ActivityImportMedsBinding
import com.carelon.whe.ui.add_medication.AddMedicationActivity
import com.carelon.whe.ui.email_verification.EmailVerificationActivity
import com.carelon.whe.util.launch
import com.carelon.whe.util.setSafeOnClickListener
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity class that list down what all you can do when you import your meds
 */
@AndroidEntryPoint
class ImportMedsActivity : BaseActivity<ActivityImportMedsBinding,ImportMedsViewModel>() {

    private val importMedsViewModel: ImportMedsViewModel by viewModels()

    override fun getContentView(): Int = R.layout.activity_import_meds

    override fun getBindingVariable(): Int = BR.importMedsViewModel

    override fun getViewModel(): ImportMedsViewModel  = importMedsViewModel

    override fun showLightStatusBar(): Boolean = true

    private var binding: ActivityImportMedsBinding? = null

    private var focusStepId : Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        focusStepId = intent.getIntExtra(BundleKeyConstants.EXTRA_FOCUS_AREA_ID, 0)
        binding = getDataBinding()
        initOnClicks()
    }

    private fun initOnClicks() {
        binding?.btnImportMeds?.setSafeOnClickListener {
            startActivityResult.launch(Intent(this, AddMedicationActivity::class.java))
        }

        binding?.txtNotRightNow?.setSafeOnClickListener {
            medsImported()
        }

        binding?.imgClose?.setSafeOnClickListener {
            medsImported()
        }
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                medsImported()
            }
        })
    }

    //Callback for the launched activity to check if medication has been selected
    private val startActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            medsImported(result)
        }
    }

    // Return selected medicine size to previous requested screen
    private fun medsImported(result: ActivityResult?=null) {
        val resultIntent = Intent()
        resultIntent.putExtra(
            BundleKeyConstants.EXTRA_FOCUS_AREA_ID,focusStepId
        )
        if(result==null){
            resultIntent.putExtra(
                BundleKeyConstants.EXTRA_IMPORT_MEDICINES_NOT_NOW, true
            )
        }else{
            result.let {
                resultIntent.putExtra(
                    BundleKeyConstants.EXTRA_IMPORT_MEDICINES_COUNT,
                    it.data?.getIntExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES_COUNT, 0)
                )
                resultIntent.putExtra(
                    BundleKeyConstants.EXTRA_IMPORT_MEDICINES,
                    it.data?.getStringExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES)
                )
            }
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}