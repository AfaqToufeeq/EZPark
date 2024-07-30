package com.admin.ezpark.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.databinding.ItemDashboardCardBinding

class DashboardAdapter(private val callback:(DashboardCard) -> Unit) :
    ListAdapter<DashboardCard, DashboardAdapter.DashboardViewHolder>(DashboardDiffCallback()) {


    inner class DashboardViewHolder(private val binding: ItemDashboardCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(card: DashboardCard) {
                binding.apply {
                    tvCardTitle.text = card.title
                    tvCardContent.text = card.content
                    root.setOnClickListener { callback.invoke(card) }
                }

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding = ItemDashboardCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DashboardDiffCallback : DiffUtil.ItemCallback<DashboardCard>() {
        override fun areItemsTheSame(oldItem: DashboardCard, newItem: DashboardCard): Boolean {
            return oldItem.title == newItem.title && oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: DashboardCard, newItem: DashboardCard): Boolean {
            return oldItem == newItem
        }

    }
}
