package com.carelon.whe.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

@Suppress("UNCHECKED_CAST")
abstract class BaseViewHolder <T>(
    binding: ViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
    var itemClickCallback: ItemClickedCallback<T?>? = null
    var positionCallback: ItemClickedPositionCallback? = null
    var positionCallbackWithData: ItemClickedCallBackWithPosition<T?>? = null

    abstract fun bind(
        data: T,
        position: Int
    )
    interface ItemClickedCallback<T> {
        fun onItemClicked(t: T)
    }
    interface ItemClickedPositionCallback {
        fun onItemOfPositionClicked(t: Int)
    }
    interface ItemClickedCallBackWithPosition<T> {
        fun onItemClicked(data: T, pos: Int)
    }
}