package com.carelon.whe.ui.medicine_reminder

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.constants.Constants
import com.carelon.whe.databinding.ActivityMedicineReminderBinding
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.model.TimePickerItem
import com.carelon.whe.ui.medicine_reminder.scheduler.AlarmScheduler
import com.carelon.whe.util.*
import com.carelon.whe.widget.custom_typeface.CustomTypeface
import com.carelon.whe.widget.wheel_picker.BaseWheelPickerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MedicineReminderActivity :
    BaseActivity<ActivityMedicineReminderBinding, MedicineReminderViewModel>() {

    private val medicineReminderViewModel by viewModels<MedicineReminderViewModel>()

    override fun getContentView(): Int = R.layout.activity_medicine_reminder

    override fun getBindingVariable(): Int = BR.medicineReminderViewModel

    override fun getViewModel(): MedicineReminderViewModel = medicineReminderViewModel

    override fun showLightStatusBar(): Boolean = true

    private var binding: ActivityMedicineReminderBinding? = null

    private var reminderExistJob: Job? = null

    @Inject
    lateinit var hoursAdapter: TimePickerAdapter

    @Inject
    lateinit var minutesAdapter: TimePickerAdapter

    @Inject
    lateinit var formatAdapter: FormatPickerAdapter

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    companion object {
        const val HOUR = 12
        const val MINUTES = 0
        const val FORMAT = Calendar.PM
    }

    private var mHour = HOUR
    private var mMinutes = MINUTES
    private var mFormat = FORMAT

    private val calendar: Calendar by lazy {
        Calendar.getInstance()
    }

    private val hour: Int by lazy {
        if (editMedicineReminder) {
            medicineReminder?.hour ?: HOUR
        } else {
            val hr = calendar.get(Calendar.HOUR)
            val mins = calendar.get(Calendar.MINUTE)
            if (mins in 54..60) {
                hr.plus(1)
            } else if (hr == 0) {
                hr.plus(12)
            } else {
                hr
            }
        }
    }

    private val minutes: Int by lazy {
        if (editMedicineReminder) {
            medicineReminder?.minutes ?: MINUTES
        } else {
            when (calendar.get(Calendar.MINUTE)) {
                in 8..23 -> {
                    15
                }
                in 24..37 -> {
                    30
                }
                in 38..53 -> {
                    45
                }
                else -> {
                    0
                }
            }
        }
    }

    private val format: Int by lazy {
        if (editMedicineReminder) {
            medicineReminder?.format ?: FORMAT
        } else {
            val hr = calendar.get(Calendar.HOUR)
            val mins = calendar.get(Calendar.MINUTE)
            if (hr == 11 && mins in 54..60 && calendar.get(Calendar.AM_PM) == Calendar.AM) {
                Calendar.PM
            } else if (hr == 11 && mins in 54..60 && calendar.get(Calendar.AM_PM) == Calendar.PM) {
                Calendar.AM
            } else {
                calendar.get(Calendar.AM_PM)
            }
        }
    }

    private val medicineReminder: MedicineReminder? by lazy {
        intent.parcelable(BundleKeyConstants.EXTRA_MEDICINE_REMINDER_ITEM)
    }

    private val medicineReminderList: ArrayList<MedicineReminder> by lazy {
        intent.parcelableArrayList(BundleKeyConstants.EXTRA_MEDICINE_REMINDERS_LIST)
            ?: arrayListOf()
    }

    private val showReminderList: Boolean by lazy {
        intent.getBooleanExtra(BundleKeyConstants.EXTRA_SHOW_EXISTING_REMINDER_LIST, false)
    }

    private val editMedicineReminder: Boolean by lazy {
        intent.getBooleanExtra(BundleKeyConstants.EXTRA_EDIT_MEDICINE_REMINDER, false)
    }

    private val alarmManager: AlarmManager by lazy {
        getSystemService(AlarmManager::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        subscribeObservers()
        initOnClicks()
        initTitle()
    }

    /**
     * Initialize the UI based on notification permission status
     */
    override fun onResume() {
        super.onResume()
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            showPermissionNeededUI()
        } else {
            showMedicineReminderUI()
        }
    }

    override fun onPause() {
        super.onPause()
        removeListener()
    }

    /**
     * Remove listener when app goes in background
     */
    private fun removeListener() {
        binding?.run {
            pickerHour.removeListener()
            pickerMinutes.removeListener()
            pickerFormat.removeListener()
        }
    }

    /**
     * Checking if permission is already granted or not
     */
    private fun checkIfNotificationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (hasPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                insertMedicineReminderToDatabase()
            } else {
                requestRuntimePermission()
            }
        } else {
            insertMedicineReminderToDatabase()
        }
    }

    /**
     * Request notification permission
     */
    private fun requestRuntimePermission() {
        requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    /**
     * Show permission allowed UI and initialize all the views
     */
    private fun showMedicineReminderUI() {
        binding?.groupPermissionDenied?.gone()
        binding?.groupPermissionAllowed?.show()
        setHoursAdapterToUI()
        setMinutesAdapterToUI()
        setFormatAdapterToUI()
        initListener()
        setExistingReminderUI()
        initDeleteUI()
    }

    /**
     * If edit then update the item in database else insert the item
     */
    private fun insertMedicineReminderToDatabase() {
        if (alarmManager.canScheduleAlarms()) {
            val id = if (editMedicineReminder) {
                medicineReminder?.id ?: System.currentTimeMillis().toInt()
            } else {
                System.currentTimeMillis().toInt()
            }
            val medicineReminder = MedicineReminder(
                id = id,
                hour = mHour,
                minutes = mMinutes,
                format = mFormat
            )
            alarmScheduler.scheduleAlarm(medicineReminder)
            if (editMedicineReminder) {
                medicineReminderViewModel.updateMedicineReminder(medicineReminder)
            } else {
                medicineReminderViewModel.addMedicineReminder(medicineReminder)
            }
        }
    }

    /**
     * Show permission denied UI
     */
    private fun showPermissionNeededUI() {
        binding?.groupPermissionAllowed?.gone()
        binding?.groupPermissionDenied?.show()
    }

    /**
     * Return medicine reminder time to previous screen and close the activity after inserting the data in database
     */
    private fun subscribeObservers() {
        medicineReminderViewModel.eventFlow.observe(this) { event ->
            val resultIntent = Intent()
            when (event) {
                is MedicineReminderViewModel.UiEvent.AddReminder -> {
                    resultIntent.putExtra(
                        BundleKeyConstants.EXTRA_MEDICINE_REMINDER_TIME_SELECTED,
                        "${Constants.ADDED_REMINDER_FOR} ${event.medicineReminder}"
                    )
                }
                is MedicineReminderViewModel.UiEvent.DeleteReminder -> {
                    resultIntent.putExtra(
                        BundleKeyConstants.EXTRA_MEDICINE_REMINDER_TIME_SELECTED,
                        getString(R.string.reminder_deleted)
                    )
                }
                else -> {
                    resultIntent.putExtra(
                        BundleKeyConstants.EXTRA_MEDICINE_REMINDER_TIME_SELECTED,
                        ""
                    )
                }
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }

    private fun initOnClicks() {
        /**
         * Checking if notification permission allowed then insert the item in database and set the alarm otherwise ask for permission
         */
        binding?.btnMedicineReminder?.setSafeOnClickListener {
            checkIfNotificationPermissionGranted()
        }

        /**
         * Delete the notification reminder from database and cancel the alarm
         */
        binding?.txtDelete?.setSafeOnClickListener {
            medicineReminder?.let { _medicineReminder ->
                alarmScheduler.cancelAlarm(_medicineReminder)
                medicineReminderViewModel.deleteMedicineReminder(_medicineReminder)
            }
        }

        /**
         * Finish the current activity
         */
        binding?.imgClose?.setSafeOnClickListener {
            finish()
        }

        /**
         * Take user to app settings page to manually allow the notification permission
         */
        binding?.constraintSettings?.setSafeOnClickListener {
            val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(settingsIntent)
        }
    }

    /**
     * Set the title based on the reminder type
     */
    private fun initTitle() {
        if (editMedicineReminder) {
            binding?.txtTitle?.text = getString(R.string.edit_reminder)
        } else {
            binding?.txtTitle?.text = getString(R.string.add_reminder)
        }
    }

    /**
     * Assigning hours values to the recyclerview and making it circular
     */
    private fun setHoursAdapterToUI() {
        val scrollPosition = medicineReminderViewModel.getHourScrollPosition(hour)
        hoursAdapter.addAll(medicineReminderViewModel.getHoursList())
        binding?.pickerHour?.setAdapter(hoursAdapter)
        binding?.pickerHour?.setSelectedIndex(scrollPosition)
        binding?.pickerHour?.setCircular()
        hoursAdapter.notifyDataSetChanged()
    }

    /**
     * Assigning minutes values to the recyclerview and making it circular
     */
    private fun setMinutesAdapterToUI() {
        val scrollPosition =
            medicineReminderViewModel.getMinutesScrollPosition(minutes)
        minutesAdapter.addAll(medicineReminderViewModel.getMinutesList())
        binding?.pickerMinutes?.setAdapter(minutesAdapter)
        binding?.pickerMinutes?.setSelectedIndex(scrollPosition)
        binding?.pickerMinutes?.setCircular()
        minutesAdapter.notifyDataSetChanged()

    }

    /**
     * Assigning format values to the recyclerview
     */
    private fun setFormatAdapterToUI() {
        val scrollPosition =
            medicineReminderViewModel.getFormatPosition(format)
        formatAdapter.addAll(medicineReminderViewModel.getFormatList())
        binding?.pickerFormat?.setSelectedIndex(scrollPosition)
        binding?.pickerFormat?.setAdapter(formatAdapter)
        formatAdapter.notifyDataSetChanged()
    }

    /**
     * RecyclerView callback listeners whenever user scrolls the hour, minutes or format list
     */
    private fun initListener() {
        binding?.pickerHour?.setWheelListener(object : BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(index: Int) {
                val position = index % hoursAdapter.listSize
                mHour = hoursAdapter.list.getOrNull(position)?.value ?: HOUR
                val list = hoursAdapter.list
                highlightSelectedView(list, position)
                binding?.pickerHour?.post {
                    hoursAdapter.notifyDataSetChanged()
                }
            }
        })

        binding?.pickerMinutes?.setWheelListener(object :
            BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(index: Int) {
                val position = index % minutesAdapter.listSize
                mMinutes = minutesAdapter.list.getOrNull(position)?.value ?: MINUTES
                val list = minutesAdapter.list
                highlightSelectedView(list, position)
                binding?.pickerMinutes?.post {
                    minutesAdapter.notifyDataSetChanged()
                }
            }
        })

        binding?.pickerFormat?.setWheelListener(object :
            BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(index: Int) {
                mFormat = formatAdapter.list.getOrNull(index)?.value ?: FORMAT
                val list = formatAdapter.list
                highlightSelectedView(list, index)
                binding?.pickerFormat?.post {
                    formatAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    /**
     * Highlight the selected item of the recyclerview and set values to reminder button
     */
    private fun highlightSelectedView(list: MutableList<TimePickerItem>, position: Int) {
        list.map { item ->
            item.isSelected = false
        }
        list.getOrNull(position)?.isSelected = true
        checkIfReminderExists()
    }

    /**
     * Show list of existing reminder in the UI
     */
    private fun setExistingReminderUI() {
        if (showReminderList) {
            lifecycleScope.launch(Dispatchers.Main) {
                val mediumFont = ResourcesCompat.getFont(applicationContext, R.font.sans_medium)
                val semiBoldFont = ResourcesCompat.getFont(applicationContext, R.font.sans_semibold)
                safeLet(mediumFont, semiBoldFont) { _mediumFont, _semiBoldFont ->
                    val existingMedicineReminder = "${Constants.REMINDER_SET_FOR} ${
                        medicineReminderViewModel.getExistingMedicineReminderString(
                            medicineReminderList
                        )
                    }"
                    val reminderSetTextLength = Constants.REMINDER_SET_FOR.length
                    val spanner = SpannableStringBuilder(existingMedicineReminder)
                    spanner.setSpan(
                        CustomTypeface(_mediumFont),
                        0,
                        reminderSetTextLength,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spanner.setSpan(
                        CustomTypeface(_semiBoldFont),
                        reminderSetTextLength,
                        existingMedicineReminder.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val subStringToFind = "and"
                    val startIndex = existingMedicineReminder.indexOf(
                        subStringToFind,
                        startIndex = reminderSetTextLength
                    )
                    val endIndex = startIndex + subStringToFind.length
                    if (startIndex != -1) {
                        spanner.setSpan(
                            CustomTypeface(_mediumFont),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    binding?.txtExistingReminder?.show()
                    binding?.txtExistingReminder?.text = spanner
                }
            }
        }
    }

    /**
     * Enabled or Disabled the reminder button based of if there is already a reminder exist or not
     */
    private fun checkIfReminderExists() {
        var ifReminderExists = false
        if (showReminderList) {
            reminderExistJob?.cancel()
            reminderExistJob = lifecycleScope.launch(Dispatchers.Main) {
                ifReminderExists = medicineReminderViewModel.checkReminderExists(
                    mHour,
                    mMinutes,
                    mFormat,
                    medicineReminderList
                )
                if (ifReminderExists) {
                    binding?.btnMedicineReminder?.isEnabled = false
                    binding?.btnMedicineReminder?.backgroundTintList =
                        ContextCompat.getColorStateList(
                            this@MedicineReminderActivity,
                            R.color.light_gray_add_medication
                        )
                } else {
                    binding?.btnMedicineReminder?.isEnabled = true
                    binding?.btnMedicineReminder?.backgroundTintList =
                        ContextCompat.getColorStateList(
                            this@MedicineReminderActivity,
                            R.color.button_purple
                        )
                }
                medicineReminderViewModel.addReminderButtonText(
                    mHour,
                    mMinutes,
                    mFormat,
                    ifReminderExists,
                    editMedicineReminder
                )
            }
        } else {
            medicineReminderViewModel.addReminderButtonText(
                mHour,
                mMinutes,
                mFormat,
                ifReminderExists,
                editMedicineReminder
            )
        }
    }

    /**
     * Function to hide or show the delete reminder text
     */
    private fun initDeleteUI() {
        if (editMedicineReminder) {
            binding?.txtDelete?.show()
        } else {
            binding?.txtDelete?.gone()
        }
    }

    /**
     * Notification permission callback function to identify whether user allow or denied the permission request
     */
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                insertMedicineReminderToDatabase()
            } else {
                showPermissionNeededUI()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}