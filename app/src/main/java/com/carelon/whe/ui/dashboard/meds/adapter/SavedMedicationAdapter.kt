package com.carelon.whe.ui.dashboard.meds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carelon.whe.base.*
import com.carelon.whe.databinding.ItemSavedMedicationBinding
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.util.setSafeOnClickListener
import javax.inject.Inject

/**
 * Adapter class for listing saved medication that set by user
 */
class SavedMedicationAdapter @Inject constructor(): BaseAdapter<Medicine, SavedReminderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedReminderViewHolder {
        return SavedReminderViewHolder(ItemSavedMedicationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SavedReminderViewHolder, position: Int) {
        holder.bind(list[position], position)
        holder.positionCallbackWithData = clickListenerWithPosition
    }

    override fun getItemCount(): Int = listSize
}

class SavedReminderViewHolder(val binding: ItemSavedMedicationBinding) : BaseViewHolder<Medicine?>(binding){
    override fun bind(data: Medicine?, position: Int) {
        binding.medicineDataModel = data
        binding.executePendingBindings()
        binding.root.setSafeOnClickListener {
            positionCallbackWithData?.onItemClicked(data, position)
        }
    }
}