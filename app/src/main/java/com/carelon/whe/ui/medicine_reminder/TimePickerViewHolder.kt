package com.carelon.whe.ui.medicine_reminder

import com.carelon.whe.R
import com.carelon.whe.base.BaseViewHolder
import com.carelon.whe.databinding.ItemTimePickerBinding
import com.carelon.whe.domain.model.TimePickerItem
/**
 * ViewHolder class for displaying hour and minutes values in recyclerview items
 */
class TimePickerViewHolder (private val binding:ItemTimePickerBinding) :
    BaseViewHolder<TimePickerItem>(binding) {

    override fun bind(data: TimePickerItem, position: Int) {
        binding.txtValue.text = if (data.value == 0) {
            String.format("%02d", 0)
        } else {
            data.value.toString()
        }
        if (data.isSelected) {
            binding.txtValue.setTextAppearance(R.style.time_picker_enabled)
        } else {
            binding.txtValue.setTextAppearance(R.style.time_picker_disabled)
        }
    }
}