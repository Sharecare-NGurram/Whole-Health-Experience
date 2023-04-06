package com.carelon.whe.ui.medicine_reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carelon.whe.base.BaseAdapter
import com.carelon.whe.databinding.ItemTimePickerBinding
import com.carelon.whe.domain.model.TimePickerItem
import javax.inject.Inject
/**
 * Adapter class for populating hours and minutes values in recyclerview
 */
class TimePickerAdapter @Inject constructor() : BaseAdapter<TimePickerItem, TimePickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimePickerViewHolder {
        val binding = ItemTimePickerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TimePickerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: TimePickerViewHolder, position: Int) {
        val pos = position % listSize
        holder.bind(list[pos],pos)
    }
}