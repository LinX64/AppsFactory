/*
 * *
 *  * Created by Mohsen on 10/26/22, 3:54 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 10/26/22, 3:54 PM
 *
 */

package com.example.appsfactory.presentation.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T : Any, VB : ViewBinding> :
    ListAdapter<T, BaseListAdapter.ViewBindingVH<VB>>(BaseDiffUtil<T>()) {

    class ViewBindingVH<VB : ViewBinding> constructor(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)

    private class BaseDiffUtil<T : Any> : DiffUtil.ItemCallback<T>() {

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingVH<VB> {
        val view = inflateView(LayoutInflater.from(parent.context), viewType)
        return ViewBindingVH(view)
    }

    override fun onBindViewHolder(holder: ViewBindingVH<VB>, position: Int) {
        val item = getItem(position)
        bind(holder.binding, position, item)

        holder.binding.root.setOnClickListener {
            onItemClickListener(item)
        }
    }

    abstract fun inflateView(inflater: LayoutInflater, viewType: Int): VB

    abstract fun bind(binding: VB, position: Int, item: T)

    open fun onItemClickListener(item: T) {}
}