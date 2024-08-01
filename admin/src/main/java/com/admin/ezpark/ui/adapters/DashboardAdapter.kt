package com.admin.ezpark.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.admin.ezpark.R
import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.databinding.ItemDashboardCardBinding
import com.admin.ezpark.databinding.ItemDashboardCardType2Binding
import com.admin.ezpark.enums.DashboardType

class DashboardAdapter(private val callback:(DashboardCard) -> Unit) :
    ListAdapter<DashboardCard, RecyclerView.ViewHolder>(DashboardDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DashboardType.TYPE1.type -> {
                val binding = ItemDashboardCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Type1ViewHolder(binding)
            }
            DashboardType.TYPE2.type -> {
                val binding = ItemDashboardCardType2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Type2ViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is Type1ViewHolder -> holder.bind(item, callback)
            is Type2ViewHolder -> holder.bind(item, callback)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    class DashboardDiffCallback : DiffUtil.ItemCallback<DashboardCard>() {
        override fun areItemsTheSame(oldItem: DashboardCard, newItem: DashboardCard): Boolean {
            return oldItem.number == newItem.number && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: DashboardCard, newItem: DashboardCard): Boolean {
            return oldItem == newItem
        }
    }

    class Type1ViewHolder(
        private val binding: ItemDashboardCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardCard, callback: (DashboardCard) -> Unit) {
            binding.apply {
               numberTextView.text = item.number.toString()
               headingTextview.text = item.heading.toString()
                root.setOnClickListener { callback(item) }
            }
        }
    }


    class Type2ViewHolder(
        private val binding: ItemDashboardCardType2Binding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardCard, callback: (DashboardCard) -> Unit) {
            binding.apply {
                tvCardTitle.text = item.title?.name
                tvCardContent.text = item.content ?: ""
                ivCardIcon.setImageResource(item.imageResource ?: R.drawable.card_icon_placeholder)
                root.setOnClickListener { callback(item) }
            }
        }
    }
}
