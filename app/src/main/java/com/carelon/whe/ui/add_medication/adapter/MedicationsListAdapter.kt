package com.carelon.whe.ui.add_medication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carelon.whe.base.*
import com.carelon.whe.databinding.ItemMedicineBinding
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.util.setSafeOnClickListener
import javax.inject.Inject

/**
 * Adapter class for listing medicines to import
 */
class MedicationsListAdapter @Inject constructor(): BaseAdapter<MedicationData, MedicationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder(ItemMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        holder.bind(list[position], position)
        holder.positionCallbackWithData = clickListenerWithPosition
    }

    override fun getItemCount(): Int = listSize
}

class MedicationViewHolder(val binding: ItemMedicineBinding) : BaseViewHolder<MedicationData> (binding){
    override fun bind(data: MedicationData, position: Int) {
        binding.medicineDataModel = data
        binding.executePendingBindings()
        binding.root.setSafeOnClickListener {
            positionCallbackWithData?.onItemClicked(data, position)
        }
    }
}