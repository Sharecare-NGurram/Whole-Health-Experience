package com.carelon.whe.ui.dashboard.foryou.calender

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.carelon.whe.R
import com.carelon.whe.base.BaseAdapter
import com.carelon.whe.databinding.ItemCurrentWeekDayBinding
import com.carelon.whe.util.setSafeOnClickListener
import java.util.*
import javax.inject.Inject

class CurrentWeekAdapter @Inject constructor() : BaseAdapter<CurrentDayRecord, CurrentWeekAdapter.CalenderSingleHolder>() {

    var mListener : OnDateChange? = null

    private var isDatesSelectionEnabled : CalendarSelectionType= CalendarSelectionType.CURRENT_DATE_SELECTION
    private var textDateEnabledColor : Int = R.color.text_date_selected
    private var textDateDisabledColor : Int = R.color.text_date_unselected
    private var textProgressEnabledColor : Int = R.color.text_date_progress_enabled
    private var dateSelectorDrawable: Int = R.drawable.drawable_date_selector_bg

    init {
        setupCurrentWeek(Calendar.getInstance())
    }

    /*
    * Populate Current Dates of the week on the startup
    */
    private fun setupCurrentWeek(currentDate : Calendar) {
        val c: Calendar = Calendar.getInstance()
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        var isCurrentDay = if(currentDate.get(Calendar.DAY_OF_WEEK)==c.get(Calendar.DAY_OF_WEEK)) 1 else 0
        var isEnabled = if(currentDate.get(Calendar.MONTH)==c.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH)>=c.get(Calendar.DAY_OF_MONTH)) 1 else 0

        list.add(
            CurrentDayRecord(
                date = c.time, currentSelection = isCurrentDay, enabled = isEnabled,
                progress = 0,
            )
        )
        for (i in 0..5) {
            c.add(Calendar.DATE, 1)

            isCurrentDay = if(currentDate.get(Calendar.DAY_OF_WEEK)==c.get(Calendar.DAY_OF_WEEK)) 1 else 0
            isEnabled = if(currentDate.get(Calendar.MONTH)==c.get(Calendar.MONTH) &&
                currentDate.get(Calendar.DAY_OF_MONTH)>=c.get(Calendar.DAY_OF_MONTH)) 1 else 0

            list.add(
                CurrentDayRecord(
                    date = c.time, currentSelection = isCurrentDay, enabled = isEnabled,
                    progress = 0,
                )
            )
        }
    }

    /*
    * Change current date to selected tab
    */
    private fun selectCurrentDate(currentDate : Calendar){
        for (data in list){
            val c: Calendar = Calendar.getInstance()
            c.time = data!!.date

            val isCurrentDay = if(currentDate.get(Calendar.DAY_OF_WEEK)==c.get(Calendar.DAY_OF_WEEK)) 1 else 0
            data.currentSelection = isCurrentDay
        }
        notifyDataSetChanged()
    }

    /*
    * Change the week range from default current week to other week of passed date
    */
    fun changeToAnotherWeek(currentDate : Calendar){
        list.clear()
        setupCurrentWeek(currentDate)
        notifyDataSetChanged()
    }

    /*
    * Flag to enable if user can select future dates also
    */
    fun setFutureDatesEnabled(){
        isDatesSelectionEnabled = CalendarSelectionType.FUTURE_DATE_SELECTION
    }

    /*
    * Flag to enable if user can select past dates also
    */
    fun setPastDatesEnabled(){
        isDatesSelectionEnabled = CalendarSelectionType.PAST_DATE_SELECTION
    }

    fun setDateTextEnabledColor(color:Int){
        textDateEnabledColor= color
    }

    fun setDateTextDisabledColor(color:Int){
        textDateDisabledColor= color
    }

    fun setProgressTextEnabledColor(color:Int){
        textProgressEnabledColor= color
    }

    inner class CalenderSingleHolder(var binding: ItemCurrentWeekDayBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemData: CurrentDayRecord?) {
            itemData?.let {
                binding.currentDayData = it
                binding.executePendingBindings()

                if(itemData.progress==100){
                    if (itemData.currentSelection == 1) {
                        binding.progressBar.background = context.getDrawable(R.drawable.drawable_date_circular_shape_full_selected)
                    }else{
                        binding.progressBar.background = context.getDrawable(R.drawable.drawable_date_circular_shape_full)
                    }
                    binding.txtDay.setTextColor(context.getColor(R.color.white))
                }else{
                    if (itemData.currentSelection == 1) {
                        binding.progressBar.background = context.getDrawable(R.drawable.drawable_date_circular_shape_selected)
                    }else{
                        binding.progressBar.background = context.getDrawable(R.drawable.drawable_date_circular_shape)
                    }

                    if (itemData.enabled == 1 && itemData.currentSelection == 0) {
                        binding.txtDay.setTextColor(context.getColor(textProgressEnabledColor))
                    }
                    else if (itemData.currentSelection == 1) {
                        binding.txtDay.setTextColor(context.getColor(textDateEnabledColor))
                    }
                    else {
                        binding.txtDay.setTextColor(context.getColor(textDateDisabledColor))
                    }
                }

                if (itemData.currentSelection == 1) {
                    binding.root.background = context.getDrawable(dateSelectorDrawable)
                    binding.txtDayOfWeek.setTextColor(context.getColor(textDateEnabledColor))
                    val typeface = context.resources.getFont(R.font.sans_semibold)
                    binding.txtDayOfWeek.typeface = typeface
                    binding.progressBar.progressDrawable = context.getDrawable(R.drawable.drawable_date_circular_progress_selected)
                }
                else {
                    binding.root.background = null
                    binding.txtDayOfWeek.setTextColor(context.getColor(textDateDisabledColor))
                    val typeface = context.resources.getFont(R.font.sans_medium)
                    binding.txtDayOfWeek.typeface = typeface
                    binding.txtDayOfWeek.setTypeface(binding.txtDayOfWeek.typeface, Typeface.BOLD)
                    binding.progressBar.progressDrawable = context.getDrawable(R.drawable.drawable_date_circular_progress)
                }

            }
        }

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderSingleHolder {
        val itemView = ItemCurrentWeekDayBinding.inflate(LayoutInflater.from(parent.context))
        return CalenderSingleHolder(itemView,parent.context)
    }

    override fun onBindViewHolder(holder: CalenderSingleHolder, position: Int) {
        holder.bind(list[position])

        holder.binding.root.setSafeOnClickListener {
            list[position]?.let {
                if (isDatesSelectionEnabled != CalendarSelectionType.CURRENT_DATE_SELECTION) {
                    val selectedCal = Calendar.getInstance()
                    selectedCal.time = it.date

                    if ((isDatesSelectionEnabled == CalendarSelectionType.FUTURE_DATE_SELECTION && it.enabled == 0) ||
                        (isDatesSelectionEnabled == CalendarSelectionType.PAST_DATE_SELECTION && it.enabled == 1) ||
                        (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == selectedCal.get(
                            Calendar.DAY_OF_WEEK
                        ))
                    ) {
                        selectCurrentDate(selectedCal)
                        mListener?.onNewDateSelected(list[position])
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnDateChange{
        fun onNewDateSelected(data:CurrentDayRecord?)
    }
}

/**
    * value 0 -> Only current date
    * value 1 -> Future date disabled
    * value 2 -> Past Date disabled
    * */
enum class CalendarSelectionType(value:Int){
    CURRENT_DATE_SELECTION(0),
    FUTURE_DATE_SELECTION(1),
    PAST_DATE_SELECTION(2),
}