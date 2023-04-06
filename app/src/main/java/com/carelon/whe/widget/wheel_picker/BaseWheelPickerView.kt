package com.carelon.whe.widget.wheel_picker

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.carelon.whe.base.BaseAdapter
import com.carelon.whe.base.BaseViewHolder

class BaseWheelPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    WheelPickerRecyclerView.WheelPickerRecyclerViewListener {


    interface WheelPickerViewListener {
        fun didSelectItem(index: Int)
        fun onScrollStateChanged(state: Int) {}
    }

    private var mSelectedIndex = NO_POSITION


    fun setSelectedIndex(position: Int, ) {
        mSelectedIndex = position
        recyclerView.scrollToCenterPosition(
            position,
            ignoreHapticFeedback = true,
            completion = null
        )
    }

    fun setCircular() {
        val selectedIndex = mSelectedIndex
        val completion = {
            recyclerView.refreshCurrentPosition()
        }
        val valueCount = (recyclerView.adapter as? BaseAdapter<*,*>)?.listSize ?: 0
        if (valueCount > 0) {
            recyclerView.scrollToCenterPosition(
                ((Int.MAX_VALUE / 2) / valueCount) * valueCount + selectedIndex,
                true,
                completion
            )
        } else {
            recyclerView.scrollToCenterPosition(selectedIndex, true, completion)
        }
    }

    private val recyclerView: WheelPickerRecyclerView by lazy {
        val recyclerView = WheelPickerRecyclerView(context)
        addView(recyclerView)
        recyclerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        recyclerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                listener?.onScrollStateChanged(newState)
            }
        })
        recyclerView
    }

    fun <Element : Any, ViewHolder : BaseViewHolder<Element>> setAdapter(adapter: BaseAdapter<Element, ViewHolder>) {
        recyclerView.adapter = adapter
    }

    private var listener: WheelPickerViewListener? = null

    fun setWheelListener(listener: WheelPickerViewListener) {
        recyclerView.setWheelListener(this)
        this.listener = listener
    }

    fun removeListener() {
        recyclerView.removeListener()
        listener = null
    }

    override fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled)
        recyclerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
    }

    init {
        recyclerView
    }

    override fun didSelectItem(position: Int) {
        listener?.didSelectItem(position)
    }
}