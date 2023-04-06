package com.carelon.whe.ui.app_feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carelon.whe.base.*
import com.carelon.whe.databinding.ItemAppFeaturesBinding
import com.carelon.whe.domain.model.AppFeatureModel
import javax.inject.Inject

/**
 * Adapter class for listing app features currently providing
 */
class AppFeaturesAdapter @Inject constructor(): BaseAdapter<AppFeatureModel, AppFeaturesAdapter.AppFeaturesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppFeaturesViewHolder {
        return AppFeaturesViewHolder(ItemAppFeaturesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AppFeaturesViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = listSize

    inner class AppFeaturesViewHolder(val binding: ItemAppFeaturesBinding) : BaseViewHolder<AppFeatureModel?>(binding){
        override fun bind(data: AppFeatureModel?, position: Int) {
            binding.featureDataModel = data
            binding.executePendingBindings()
        }
    }
}