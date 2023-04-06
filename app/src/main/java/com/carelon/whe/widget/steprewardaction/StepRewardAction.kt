package com.carelon.whe.widget.steprewardaction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.carelon.whe.R
import com.carelon.whe.util.gone
import com.carelon.whe.util.setSafeOnClickListener
import com.carelon.whe.util.show

class StepRewardAction(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    var txtHeader : TextView
    var txtSubHeader : TextView
    var txtReward : TextView
    var txtStepSuccess : TextView
    var btnAction : Button

    init {
        inflate(context, R.layout.view_reward_action, this)

        txtHeader = findViewById(R.id.txt_header)
        txtSubHeader  = findViewById(R.id.txt_subHeader)
        txtReward  = findViewById(R.id.txt_part_of_reward)
        txtStepSuccess  = findViewById(R.id.txt_step_success)
        btnAction = findViewById(R.id.btn_action)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StepRewardAction)
        txtHeader.text=attributes.getString(R.styleable.StepRewardAction_text_header)
        txtSubHeader.text=attributes.getString(R.styleable.StepRewardAction_text_sub_header)
        txtReward.text=attributes.getString(R.styleable.StepRewardAction_text_reward)
        btnAction.text = attributes.getString(R.styleable.StepRewardAction_button_text)
        /*btnAction.backgroundTintList = ContextCompat.getColorStateList(getApplicationContext(),
            R.styleable.StepRewardAction_button_background_color)*/
        if(attributes.getString(R.styleable.StepRewardAction_text_sub_header).isNullOrEmpty())
            txtSubHeader.gone()
        if(attributes.getString(R.styleable.StepRewardAction_text_reward).isNullOrEmpty())
            txtReward.gone()

        attributes.recycle()
    }

    fun setOnActionClickListener(callback:()->Unit){
        btnAction.setSafeOnClickListener {
            callback()
        }
    }

    fun markStepCompleted(flag:Boolean){
        if(flag) {
            txtSubHeader.gone()
            txtReward.gone()
            btnAction.gone()
            txtStepSuccess.show()
        }else {
            txtSubHeader.show()
            txtReward.show()
            btnAction.show()
            txtStepSuccess.gone()
        }
    }

}