package com.carelon.whe.ui.dashboard.meds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carelon.whe.base.*
import com.carelon.whe.databinding.ItemMedicineReminderAddBinding
import com.carelon.whe.databinding.ItemMedicineReminderBinding
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.util.setSafeOnClickListener
import javax.inject.Inject

/**
 * Adapter class for listing medicines to import
 */
class MedicineRemindersAdapter @Inject constructor() :
    BaseAdapter<MedicineReminder, RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_NAVIGATE = 1
        private const val VIEW_TYPE_REMINDER = 2
    }

    var listenerCallback: ItemAddClickedCallBack<MedicineReminder>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NAVIGATE) {
            ReminderAddViewHolder(
                ItemMedicineReminderAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ReminderViewHolder(
                ItemMedicineReminderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ReminderViewHolder) {
            holder.bind(list[position], position)
            holder.listenerCallback = listenerCallback
        } else if (holder is ReminderAddViewHolder) {
            holder.bind()
            holder.listenerCallback = listenerCallback
        }
    }

    override fun getItemCount(): Int {
        return if (listSize < 9) {
            listSize.plus(1)
        } else {
            listSize
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listSize) {
            VIEW_TYPE_NAVIGATE
        } else {
            VIEW_TYPE_REMINDER
        }

    }

    interface ItemAddClickedCallBack<T> {
        fun onItemClicked(data: T, pos: Int)
        fun onAddNewItem()
    }

    class ReminderViewHolder(val binding: ItemMedicineReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var listenerCallback: ItemAddClickedCallBack<MedicineReminder>? = null
        fun bind(data: MedicineReminder, position: Int) {
            binding.reminderData = data
            binding.executePendingBindings()
            binding.root.setSafeOnClickListener {
                listenerCallback?.onItemClicked(data, position)
            }
        }
    }

    class ReminderAddViewHolder(val binding: ItemMedicineReminderAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var listenerCallback: ItemAddClickedCallBack<MedicineReminder>? =
            null

        fun bind() {
            binding.root.setSafeOnClickListener {
                listenerCallback?.onAddNewItem()
            }
        }
    }
}