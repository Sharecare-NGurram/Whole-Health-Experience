package com.carelon.whe.ui.medicine_reminder
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewHolder
import com.carelon.whe.constants.Constants
import com.carelon.whe.databinding.ItemTimePickerBinding
import com.carelon.whe.domain.model.TimePickerItem
import java.util.*

/**
 * ViewHolder class for displaying format values in recyclerview items
 */
class FormatPickerViewHolder(private val binding: ItemTimePickerBinding) :
    BaseViewHolder<TimePickerItem>(binding) {

    override fun bind(data: TimePickerItem, position: Int) {
        binding.txtValue.text = when (data.value) {
            Calendar.AM -> {
                Constants.TEXT_AM
            }
            else -> {
                Constants.TEXT_PM
            }
        }
        if (data.isSelected) {
            binding.txtValue.setTextAppearance(R.style.time_picker_enabled)
        } else {
            binding.txtValue.setTextAppearance(R.style.time_picker_disabled)
        }
    }
}