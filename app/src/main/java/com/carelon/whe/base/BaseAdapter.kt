package com.carelon.whe.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdapter<V : Any, T : RecyclerView.ViewHolder>() :
    RecyclerView.Adapter<T>() {
    var list: MutableList<V> = Collections.synchronizedList(mutableListOf())
    val listSize: Int
        get() = list.size
    var clickListener: BaseViewHolder.ItemClickedCallback<V>?=null
    var positionListener: BaseViewHolder.ItemClickedPositionCallback?=null
    var clickListenerWithPosition: BaseViewHolder.ItemClickedCallBackWithPosition<V?>?=null
    /**
     * clear all the list and add the whole list
     *
     * @param list
     */
    fun addAll(listToAdd: List<V>) {
        this.list.clear()
        this.list.addAll(listToAdd)
        differ.submitList(this.list)

    }
    fun appendList(listToAppend: List<V>) {
        this.list.addAll(listToAppend)
        differ.submitList(this.list)
    }
    fun clearList() {
        this.list.clear()
        notifyChange()
    }
    /**
     * add the element to the list
     *
     * @param v
     */
    fun add(v: V) {
        list.add(v)
    }
    fun updateDataWithPos(data: V, pos: Int) {
        this.list[pos] = data
        differ.submitList(this.list)
        notifyItemChanged(pos)
    }
    fun removeDataWithPos(pos: Int){
        this.list.removeAt(pos)
        notifyItemRemoved(pos)
    }
    @SuppressLint("NotifyDataSetChanged")
    @Synchronized
    fun notifyChange() {
        super.notifyDataSetChanged()
    }

    private val differCallback = object : DiffUtil.ItemCallback<V>(){
        override fun areItemsTheSame(oldItem: V, newItem: V): Boolean {
            return  oldItem.toString() == newItem.toString()
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: V, newItem: V): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
}