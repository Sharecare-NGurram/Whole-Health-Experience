package com.carelon.whe.ui.medicine_reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carelon.whe.base.BaseAdapter
import com.carelon.whe.databinding.ItemTimePickerBinding
import com.carelon.whe.domain.model.TimePickerItem
import javax.inject.Inject
/**
 * Adapter class for populating format values in recyclerview
 */
class FormatPickerAdapter @Inject constructor() : BaseAdapter<TimePickerItem, FormatPickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormatPickerViewHolder {
        val binding = ItemTimePickerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FormatPickerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSize
    }

    override fun onBindViewHolder(holder: FormatPickerViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

}