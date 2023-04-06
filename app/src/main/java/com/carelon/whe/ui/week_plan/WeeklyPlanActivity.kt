package com.carelon.whe.ui.week_plan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.constants.Constants
import com.carelon.whe.databinding.ActivityWeeklyPlanBinding
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.ui.add_medication.AddMedicationActivity
import com.carelon.whe.util.*
import com.carelon.whe.widget.span.span
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity class to show user weekly plan selections
 */
@AndroidEntryPoint
class WeeklyPlanActivity : BaseActivity<ActivityWeeklyPlanBinding, WeeklyPlanViewModel>() {

    private val weeklyPlanViewModel: WeeklyPlanViewModel by viewModels()
    override fun getContentView(): Int = R.layout.activity_weekly_plan

    override fun getBindingVariable(): Int = BR.weeklyPlanViewModel

    override fun getViewModel(): WeeklyPlanViewModel = weeklyPlanViewModel

    override fun showLightStatusBar(): Boolean = true

    private var binding: ActivityWeeklyPlanBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        observeDataChange()
        initOnClicks()
        initDailyCheckInUI()
        fetchInitialData()
    }

    private fun observeDataChange() {
        weeklyPlanViewModel.trackMedsStatus.observe(this){
            updateTrackMedsStatus(it)
        }
        weeklyPlanViewModel.trackYourStepsStatus.observe(this){
            updateTrackStepStatus(it)
        }
    }

    /**
     * update the UI based on track meds status
     */
    private fun updateTrackMedsStatus(currentStatus:Int) {
        binding?.run {
            when (currentStatus) {
                TackMedicationStatus.MEDICATION_IMPORT_OK.value -> {
                    checkMeds.isChecked=true
                    txtMeds.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.dark_gray
                        )
                    )
                    txtMedsDesc.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.medium_gray
                        )
                    )
                    txtMedsDesc.text = getString(R.string.str_track_your_meds_desc)
                    txtManuallyTracked.show()
                    txtAddMedications.gone()
                }
                TackMedicationStatus.MEDICATION_IMPORT_NOT_NOW.value -> {
                    checkMeds.isChecked=true
                    txtMeds.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.dark_gray
                        )
                    )
                    txtMedsDesc.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.medium_gray
                        )
                    )
                    txtMedsDesc.text = getString(R.string.str_add_meds_for_tracking)
                    txtManuallyTracked.gone()
                    txtAddMedications.show()
                }
                TackMedicationStatus.MEDICATION_NOT_CHOOSE_AS_FOCUS_AREA.value -> {
                    checkMeds.isChecked=false
                    txtMeds.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.dark_gray
                        )
                    )
                    txtMedsDesc.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.medium_gray
                        )
                    )
                    txtMedsDesc.text = getString(R.string.str_add_meds_for_tracking)
                    txtManuallyTracked.gone()
                    txtAddMedications.show()
                }
            }
        }
    }

    /**
     * update the UI based on track steps status
     * */
    private fun updateTrackStepStatus(currentStatus:Int) {
        binding?.run {
            when (currentStatus) {
                TrackYourStepsStatus.TRACK_YOUR_STEPS_OK.value -> {
                    checkSteps.isChecked=true
                    txtSteps.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.dark_gray
                        )
                    )
                    txtStepsDesc.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.medium_gray
                        )
                    )
                    txtStepsDesc.text = getString(R.string.str_track_your_steps_desc)
                    txtAutomaticallyTracked.show()
                    btnConnectApp.gone()
                    txtHealthSettings.gone()
                }
                TrackYourStepsStatus.TRACK_YOUR_STEPS_DENIED.value -> {
                    checkSteps.isChecked=false
                    txtSteps.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.toast_error_bg
                        )
                    )
                    txtStepsDesc.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.toast_error_bg
                        )
                    )
                    txtStepsDesc.text = getString(R.string.str_health_permission_not_allowed)
                    txtAutomaticallyTracked.gone()
                    btnConnectApp.gone()
                    txtHealthSettings.show()
                }
                TrackYourStepsStatus.TRACK_YOUR_STEPS_NOT_STARTED.value -> {
                    checkSteps.isChecked=false
                    txtSteps.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.medium_gray
                        )
                    )
                    txtStepsDesc.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.medium_gray
                        )
                    )
                    txtStepsDesc.text = getString(R.string.str_health_permission_not_allowed)
                    txtAutomaticallyTracked.gone()
                    txtHealthSettings.gone()
                    btnConnectApp.show()
                }
            }
        }
    }

    /**
     * Get save data from focus area step to notify which steps are completed
     * and their current state
    * */
    private fun fetchInitialData() {
        weeklyPlanViewModel.trackMedicineStatus()
        weeklyPlanViewModel.trackYourStepStatus()
    }

    private fun initOnClicks() {
        binding?.btnGetStarted?.setSafeOnClickListener {
            //UserSessionManager.triggerWeeklyPlanActivation.value = true
            val sendBroadcastIntent = Intent().apply {
                action = Constants.PICK_FOCUS_AREA_PROCEDURE
            }
            sendBroadcast(sendBroadcastIntent)
            finish()
        }
        binding?.txtAddMedications?.setSafeOnClickListener {
            startActivityResult.launch(Intent(this, AddMedicationActivity::class.java))
        }
    }

    /**
     * update the UI based on daily check-in  status
     */
    private fun initDailyCheckInUI() {
        val dailyCheckInTime = weeklyPlanViewModel.getDailyCheckInTime()
        if (dailyCheckInTime.isNotEmpty()) {
            val description = getString(R.string.str_daily_check_in_desc, dailyCheckInTime)
            val beforeDescString = description.substringBefore(dailyCheckInTime)
            val afterDescString = description.substringAfter(dailyCheckInTime)
             val spannableText = span {
                +beforeDescString
                span {
                    text = dailyCheckInTime
                    typeface = ResourcesCompat.getFont(this@WeeklyPlanActivity, R.font.sans_semibold)
                }
                +afterDescString
            }
            binding?.run {
                txtCheckInDesc.text = spannableText
                txtCheckInTime.text= getString(R.string.str_change_check_in_time)
            }
        } else {
            binding?.run {
                txtCheckInDesc.text = getString(R.string.str_choose_check_in_time_desc)
                txtCheckInTime.text = getString(R.string.str_choose_check_in_time)
            }

        }
    }

    //Callback for the launched activity to check if medication has been selected
    private val startActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultData = result.data
            resultData?.run {
                val medicineCount =
                    getIntExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES_COUNT, 0)
                val selectedMeds = getStringExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES)
                val type = object : TypeToken<List<MedicationData>>() {}.type
                val medicationsList = Gson().fromJson(selectedMeds, type) as? ArrayList<MedicationData>
                if (!medicationsList.isNullOrEmpty()) {
                    val list =
                        medicationsList.map { dt -> Medicine.ModelMapper.from(dt) } as ArrayList<Medicine>
                    weeklyPlanViewModel.saveSelectedMeds(list)

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
                weeklyPlanViewModel.markedImportMedsCompleted()
            }
        }
    }
}