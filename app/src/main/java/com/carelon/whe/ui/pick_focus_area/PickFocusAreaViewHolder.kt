package com.carelon.whe.ui.pick_focus_area

import androidx.core.content.ContextCompat
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewHolder
import com.carelon.whe.databinding.ItemPickFocusAreaBinding
import com.carelon.whe.domain.model.FocusAreaItem
import com.carelon.whe.util.setSafeOnClickListener

class PickFocusAreaViewHolder(val binding: ItemPickFocusAreaBinding) :
    BaseViewHolder<FocusAreaItem>(binding) {

    override fun bind(data: FocusAreaItem, position: Int) {
        val context = binding.root.context
        binding.txtFocusAreaTitle.text = data.title ?: ""
        binding.txtFocusAreaDesc.text = data.description ?: ""

        when (data.id) {
            1 -> {
                binding.imgFocusArea.setImageResource(
                    if (data.isCompleted) {
                        R.drawable.ic_track_meds_selected
                    } else {
                        R.drawable.ic_track_meds
                    }
                )
            }
            2 -> {
                binding.imgFocusArea.setImageResource(
                    if (data.shouldShowDenied) {
                        R.drawable.ic_active_selected
                    }else if (data.isCompleted) {
                        R.drawable.ic_active_selected
                    } else {
                        R.drawable.ic_active
                    }

                )

            }
            else -> {
                return
            }
        }

        binding.root.setSafeOnClickListener {
            if (!data.isCompleted)
                positionCallbackWithData?.onItemClicked(data, position)
        }
        binding.root.background = if (data.shouldShowDenied) {
            ContextCompat.getDrawable(context, R.drawable.drawable_saved_meds_bg)
        }else if (data.isCompleted) {
            ContextCompat.getDrawable(context, R.drawable.drawable_saved_meds_green_bg)
        } else {
            ContextCompat.getDrawable(context, R.drawable.drawable_saved_meds_bg)
        }
        binding.txtFocusAreaTitle.setTextColor(
            if (data.shouldShowDenied) {
                ContextCompat.getColor(context, R.color.medium_gray)
            }else if (data.isCompleted) {
                ContextCompat.getColor(context, R.color.text_colorSecondary)
            } else {
                ContextCompat.getColor(context, R.color.purple)
            }
        )
        binding.txtFocusAreaDesc.setTextColor(
            if (data.shouldShowDenied) {
                ContextCompat.getColor(context, R.color.medium_gray)
            }else if (data.isCompleted) {
                ContextCompat.getColor(context, R.color.text_colorSecondary)
            } else {
                ContextCompat.getColor(context, R.color.text_colorSecondaryLight)
            }
        )
        binding.txtEarnedReward.setTextColor(
            if (data.shouldShowDenied){
                ContextCompat.getColor(context, R.color.red)
            }else if (data.isCompleted) {
                ContextCompat.getColor(context, R.color.text_colorSecondary)
            } else {
                ContextCompat.getColor(context, R.color.text_colorGreen)
            }
        )
        binding.txtEarnedReward.text = if (data.shouldShowDenied){
            context.getString(R.string.str_did_not_allow_access)
        }else if (data.isCompleted) {
            context.getString(R.string.str_ready_to_earn_reward)
        } else {
            context.getString(R.string.str_earn_rewards)
        }
        binding.imgRewards.setImageResource(
            if (data.shouldShowDenied){
                R.drawable.ic_did_not_allow_access
            }else if (data.isCompleted) {
                R.drawable.ic_circle_checked_white
            } else {
                R.drawable.ic_welcome_stars
            }
        )
        binding.imgRewards.setColorFilter(
            if (data.shouldShowDenied){
                ContextCompat.getColor(context, R.color.red)
            }else if (data.isCompleted) {
                ContextCompat.getColor(context, R.color.text_colorSecondary)
            } else {
                ContextCompat.getColor(context, R.color.text_colorGreen)
            }, android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.imgFocusArea.setColorFilter(
            if (data.shouldShowDenied){
                ContextCompat.getColor(context, R.color.mid_gray)
            }else if (data.isCompleted) {
                ContextCompat.getColor(context, R.color.text_colorSecondary)
            } else {
                ContextCompat.getColor(context, R.color.text_colorGreen)
            }, android.graphics.PorterDuff.Mode.SRC_IN)
    }
}