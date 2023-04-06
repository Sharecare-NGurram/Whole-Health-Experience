package com.carelon.whe.ui.pick_focus_area

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carelon.whe.base.BaseAdapter
import com.carelon.whe.databinding.ItemPickFocusAreaBinding
import com.carelon.whe.domain.model.FocusAreaItem
import javax.inject.Inject

/**
 * Adapter class for populating focus areas data in recyclerview
 */
class PickFocusAreaAdapter @Inject constructor() :
    BaseAdapter<FocusAreaItem, PickFocusAreaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickFocusAreaViewHolder {
        val binding =
            ItemPickFocusAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickFocusAreaViewHolder(binding)
    }

    override fun getItemCount() = listSize

    override fun onBindViewHolder(holder: PickFocusAreaViewHolder, position: Int) {
        holder.bind(list[position], position)
        holder.positionCallbackWithData = clickListenerWithPosition
    }
}