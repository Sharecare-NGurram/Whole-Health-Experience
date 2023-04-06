package com.carelon.whe.ui.dashboard.meds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carelon.whe.base.*
import com.carelon.whe.databinding.ItemNewPrescriptionMedsBinding
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.util.gone
import com.carelon.whe.util.setSafeOnClickListener
import com.carelon.whe.util.show
import javax.inject.Inject

/**
 * Adapter class for listing saved medication that set by user
 */
class NewAddedMedicationAdapter @Inject constructor(): BaseAdapter<Medicine, NewAddedMedicationAdapter.NewAddedMedicationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewAddedMedicationViewHolder {
        return NewAddedMedicationViewHolder(ItemNewPrescriptionMedsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NewAddedMedicationViewHolder, position: Int) {
        holder.bind(list[position], position)
        holder.positionCallbackWithData = clickListenerWithPosition
    }

    override fun getItemCount(): Int = listSize

    inner class NewAddedMedicationViewHolder(val binding: ItemNewPrescriptionMedsBinding) : BaseViewHolder<Medicine?>(binding){
        override fun bind(data: Medicine?, position: Int) {
            binding.medicineDataModel = data
            binding.executePendingBindings()
            if(listSize==1){
                binding.checkboxSelected.gone()
            }else{
                binding.checkboxSelected.show()
            }
            binding.root.setSafeOnClickListener {
                if(listSize>1)
                    positionCallbackWithData?.onItemClicked(data, position)
            }
        }
    }
}