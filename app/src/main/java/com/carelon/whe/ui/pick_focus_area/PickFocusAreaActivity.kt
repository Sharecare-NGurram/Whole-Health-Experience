package com.carelon.whe.ui.pick_focus_area

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.base.BaseViewHolder
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.constants.Constants
import com.carelon.whe.databinding.ActivityPickFocusAreaBinding
import com.carelon.whe.domain.model.FocusAreaItem
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.ui.daily_check_in.DailyCheckInActivity
import com.carelon.whe.ui.import_meds.ImportMedsActivity
import com.carelon.whe.util.TrackYourStepsStatus
import com.carelon.whe.util.setSafeOnClickListener
import com.carelon.whe.util.showSuccessToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity class to display list of focus areas to select
 */
@AndroidEntryPoint
class PickFocusAreaActivity : BaseActivity<ActivityPickFocusAreaBinding, PickFocusAreaViewModel>() {

    private val pickFocusAreaViewModel: PickFocusAreaViewModel by viewModels()

    override fun getContentView(): Int = R.layout.activity_pick_focus_area

    override fun getBindingVariable(): Int = BR.pickFocusAreaViewModel

    override fun getViewModel(): PickFocusAreaViewModel = pickFocusAreaViewModel

    override fun showLightStatusBar(): Boolean = true

    @Inject
    lateinit var adapter: PickFocusAreaAdapter

    private var binding: ActivityPickFocusAreaBinding? = null

    private val fitnessOptions: FitnessOptions by lazy {
        FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

    private val account: GoogleSignInAccount by lazy {
        GoogleSignIn.getAccountForExtension(this, fitnessOptions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        registerReceiver(broadcastReceiver, IntentFilter(Constants.PICK_FOCUS_AREA_PROCEDURE))
        subscribeObservers()
        initOnClicks()
        fetchInitialData()
    }

    /**
     * fetch focus area steps from local
     * fetch saved medicine import status in local
     * */
    private fun fetchInitialData() {
        pickFocusAreaViewModel.getFocusAreas()
        pickFocusAreaViewModel.trackMedicineStatus()
    }

    private fun subscribeObservers() {
        pickFocusAreaViewModel.getFocusAreas.observe(this) { focusAreasList ->
            setUpAdapter(focusAreasList)
        }
        pickFocusAreaViewModel.medsImported.observe(this) {
            if (it) {
                stepCompleted(1)
            }
        }
        pickFocusAreaViewModel.trackStepActivityLD.observe(this){
            if (it) {
                stepCompleted(2)
                pickFocusAreaViewModel.updateTrackStepActivity(TrackYourStepsStatus.TRACK_YOUR_STEPS_OK.value)
            }
        }
    }

    private fun initOnClicks() {
        binding?.imgBack?.setSafeOnClickListener {
            finish()
        }

        binding?.btnContinue?.setSafeOnClickListener {
            dailyCheckInResult.launch(Intent(this,DailyCheckInActivity::class.java))
        }
    }

    /**
     * setup adapter for all the steps in your focus area, currently reading from local
     * */
    private fun setUpAdapter(focusAreasList: List<FocusAreaItem>) {
        adapter.addAll(focusAreasList)
        val trackStepActivity = pickFocusAreaViewModel.getTrackStepActivity()
        if (trackStepActivity == 1)
            stepCompleted(2)
        else if (trackStepActivity == 2)
            stepCompleted(2, true)

        binding?.recyclerFocusAreas?.adapter = adapter
        adapter.clickListenerWithPosition =
            object : BaseViewHolder.ItemClickedCallBackWithPosition<FocusAreaItem?> {
                override fun onItemClicked(data: FocusAreaItem?, pos: Int) {
                    when (data?.id) {
                        1 -> {
                            val intent =
                                Intent(this@PickFocusAreaActivity, ImportMedsActivity::class.java)
                            intent.putExtra(BundleKeyConstants.EXTRA_FOCUS_AREA_ID, data.id)
                            medsActivityResult.launch(intent)
                        }
                        2 -> {
                            showIWantToActivePopup()
                        }
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        unregisterReceiver(broadcastReceiver)
    }

    private val dailyCheckInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            pickFocusAreaViewModel.trackMedicineStatus()
            pickFocusAreaViewModel.trackMedicineStatus()
        }

    //Callback for the launched activity to check if medication has been selected
    private val medsActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultData = result.data
                resultData?.run {
                    val medicineCount =
                        getIntExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES_COUNT, 0)
                    val selectedMeds = getStringExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES)
                    val type = object : TypeToken<List<MedicationData>>() {}.type
                    val medicationsList =
                        Gson().fromJson(selectedMeds, type) as? ArrayList<MedicationData>
                    if (!medicationsList.isNullOrEmpty()) {
                        val list =
                            medicationsList.map { dt -> Medicine.ModelMapper.from(dt) } as ArrayList<Medicine>
                        pickFocusAreaViewModel.saveSelectedMeds(list)

                    if (medicineCount == 1) {
                        showSuccessToast(
                            String.format(getString(R.string.str_added_medication), medicationsList.first().getMedicineNameForToast())
                        )
                    } else {
                        showSuccessToast(
                            String.format(getString(R.string.str_added_more_than_one_medication), medicineCount)
                        )
                    }
                }
                pickFocusAreaViewModel.markedImportMedsCompleted(!getBooleanExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES_NOT_NOW,false))
            }
        }
    }

    /**
     * Mark a step completed in your focus area once the process is finished by user
     * */
    private fun stepCompleted(stepId: Int, shouldShowDenied: Boolean = false) {
        val focusAreaItem = adapter.list.firstOrNull { it.id == stepId }
        focusAreaItem?.let { _focusAreaItem ->
            _focusAreaItem.isCompleted = true
            _focusAreaItem.shouldShowDenied = shouldShowDenied
            _focusAreaItem.isCompleted=true
            adapter.updateDataWithPos(_focusAreaItem,adapter.list.indexOf(_focusAreaItem))
            updateCTA(true)
        }
    }

    /**
    * Enabled or disabled the CTA button based on step completed
    * */
    private fun updateCTA(flag:Boolean) {
        binding?.btnContinue?.isEnabled=flag
        if(flag) {
            binding?.btnContinue?.text = getString(R.string.continue_str)
            binding?.btnContinue?.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.button_purple)
        }else{
            binding?.btnContinue?.text = getString(R.string.button_pick_one_option_to_continue)
            binding?.btnContinue?.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.light_gray_add_medication)
        }
    }

    // Show popup to access health records
    private fun showIWantToActivePopup() {
        val dialog = AlertDialog.Builder(this, R.style.HealthAccessDialogTheme).apply {
            setTitle(getString(R.string.health_access_title))
            setMessage(getString(R.string.health_access_message))
            setPositiveButton(
                R.string.understood
            ) { dialog, which ->
                dialog.cancel()
                if (hasRuntimePermission()) {
                    findFitnessDataSourcesWrapper()
                } else {
                    requestRuntimePermission()
                }
            }
            setNegativeButton(
                R.string.cancel
            ) { dialog, which ->
                dialog.cancel()
            }
            setCancelable(false)
        }
        dialog.create().show()
    }

    /**
     * Check for google authentication permission
     */
    private fun hasOAuthPermission(): Boolean {
        return GoogleSignIn.hasPermissions(account, fitnessOptions)
    }

    /**
     * Check runtime permission for Google Fit Activity
     */
    private fun hasRuntimePermission(): Boolean {
        val permissionState =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestRuntimePermission() {
        requestPermission.launch(Manifest.permission.ACTIVITY_RECOGNITION)
    }

    /**
     * Request OAuth permission from Google Sign In
     */
    private fun requestOAuthPermission() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .addExtension(fitnessOptions)
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        googleAuthActivityResultLauncher.launch(signInIntent)
    }

    /**
     * Google Sign-in result to proceed the success or failure
     */
    private val googleAuthActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            task.addOnCompleteListener {
                if (task.isSuccessful) {
                    pickFocusAreaViewModel.trackStepActivity()
                }else{
                    stepCompleted(2, true)
                    pickFocusAreaViewModel.updateTrackStepActivity(TrackYourStepsStatus.TRACK_YOUR_STEPS_DENIED.value)
                }
            }
        }

    private fun findFitnessDataSourcesWrapper() {
        if (hasOAuthPermission()) {
            pickFocusAreaViewModel.trackStepActivity()
        } else {
            requestOAuthPermission()
        }
    }


    /**
     * Handle flow for requested Google Fit Activity permission
     */
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                findFitnessDataSourcesWrapper()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)){
                    stepCompleted(2, true)
                    pickFocusAreaViewModel.updateTrackStepActivity(TrackYourStepsStatus.TRACK_YOUR_STEPS_DENIED.value)
                }
            }
        }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }
}