package com.carelon.whe.ui.daily_check_in

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.Constants
import com.carelon.whe.databinding.ActivityDailyCheckInBinding
import com.carelon.whe.domain.model.DailyCheckIn
import com.carelon.whe.domain.model.TimePickerItem
import com.carelon.whe.ui.daily_check_in.dialog.DoLaterDialogFragment
import com.carelon.whe.ui.daily_check_in.dialog.SetUpFirstWeekDialogFragment
import com.carelon.whe.ui.medicine_reminder.FormatPickerAdapter
import com.carelon.whe.ui.medicine_reminder.TimePickerAdapter
import com.carelon.whe.ui.week_plan.WeeklyPlanActivity
import com.carelon.whe.util.launch
import com.carelon.whe.util.safeShow
import com.carelon.whe.util.setSafeOnClickListener
import com.carelon.whe.widget.wheel_picker.BaseWheelPickerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

/**
 * Activity class for daily check in
 */
@AndroidEntryPoint
class DailyCheckInActivity : BaseActivity<ActivityDailyCheckInBinding, DailyCheckInViewModel>() {

    private val dailyCheckInViewModel: DailyCheckInViewModel by viewModels()

    override fun getContentView(): Int = R.layout.activity_daily_check_in

    override fun getBindingVariable(): Int = BR.dailyCheckInViewModel

    override fun getViewModel(): DailyCheckInViewModel = dailyCheckInViewModel

    override fun showLightStatusBar(): Boolean = false

    @Inject
    lateinit var hoursAdapter: TimePickerAdapter

    @Inject
    lateinit var minutesAdapter: TimePickerAdapter

    @Inject
    lateinit var formatAdapter: FormatPickerAdapter

    private var binding: ActivityDailyCheckInBinding? = null

    companion object {
        const val HOUR = 7
        const val MINUTES = 0
        const val FORMAT = Calendar.PM
    }

    private var mHour = HOUR
    private var mMinutes = MINUTES
    private var mFormat = FORMAT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        registerReceiver(broadcastReceiver, IntentFilter(Constants.PICK_FOCUS_AREA_PROCEDURE))
        initOnClicks()
        setHoursAdapterToUI()
        setMinutesAdapterToUI()
        setFormatAdapterToUI()
        initListener()
    }

    private fun initOnClicks() {
        binding?.btnChooseTime?.setSafeOnClickListener {
            val dailyCheckInTime = DailyCheckIn(
                hour = mHour,
                minutes = mMinutes,
                format = mFormat
            ).toString()
            dailyCheckInViewModel.saveDailyCheckInTime(dailyCheckInTime)
            showSetUpFirstWeekDialog()
        }

        binding?.txtDoLater?.setSafeOnClickListener {
            showDoLaterDialog()
        }
    }

    private fun showSetUpFirstWeekDialog() {
        val setUpFirstWeekDialogFragment = SetUpFirstWeekDialogFragment.newInstance()
        setUpFirstWeekDialogFragment.setUpFirstWeekListener =
            object : SetUpFirstWeekDialogFragment.SetUpFirstWeekListener {
                override fun navigateToWeeklyPlanActivity() {
                    launch<WeeklyPlanActivity>()
                }
            }
        setUpFirstWeekDialogFragment.safeShow(
            supportFragmentManager,
            DailyCheckInActivity::class.java.simpleName
        )
    }

    private fun showDoLaterDialog() {
        val doLaterFragment = DoLaterDialogFragment.newInstance()
        doLaterFragment.onSkipListener = object : DoLaterDialogFragment.OnSkipListener {
            override fun navigateToWeeklyPlanActivity() {
                dailyCheckInViewModel.saveDailyCheckInTime("")
                launch<WeeklyPlanActivity>()
            }
        }
        doLaterFragment.safeShow(
            supportFragmentManager,
            DailyCheckInActivity::class.java.simpleName
        )
    }

    /**
     * Assigning hours values to the recyclerview and making it circular
     */
    private fun setHoursAdapterToUI() {
        val scrollPosition = dailyCheckInViewModel.getHourScrollPosition(mHour)
        hoursAdapter.addAll(dailyCheckInViewModel.getHoursList())
        binding?.pickerHour?.setAdapter(hoursAdapter)
        binding?.pickerHour?.setSelectedIndex(scrollPosition)
        binding?.pickerHour?.setCircular()
        hoursAdapter.notifyDataSetChanged()
    }

    /**
     * Assigning minutes values to the recyclerview and making it circular
     */
    private fun setMinutesAdapterToUI() {
        val scrollPosition = dailyCheckInViewModel.getMinutesScrollPosition(mMinutes)
        minutesAdapter.addAll(dailyCheckInViewModel.getMinutesList())
        binding?.pickerMinutes?.setAdapter(minutesAdapter)
        binding?.pickerMinutes?.setSelectedIndex(scrollPosition)
        binding?.pickerMinutes?.setCircular()
        minutesAdapter.notifyDataSetChanged()

    }

    /**
     * Assigning format values to the recyclerview
     */
    private fun setFormatAdapterToUI() {
        val scrollPosition = dailyCheckInViewModel.getFormatPosition(mFormat)
        formatAdapter.addAll(dailyCheckInViewModel.getFormatList())
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
        dailyCheckInViewModel.setDailyCheckInButtonText(
            mHour,
            mMinutes,
            mFormat
        )
        changeImageBasedOnSelectedTime()
    }

    /**
     * Show day or night image in imageview based on what checkIfTimeIsInDayOrNot function returns
     */
    private fun changeImageBasedOnSelectedTime() {
        if (dailyCheckInViewModel.checkIfTimeIsInDayOrNot(mHour, mMinutes, mFormat)) {
            binding?.imgCheckIn?.setImageResource(R.drawable.daily_check_in_day_bg)
        } else {
            binding?.imgCheckIn?.setImageResource(R.drawable.daily_check_in_night_bg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        unregisterReceiver(broadcastReceiver)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }
}