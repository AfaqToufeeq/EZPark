package com.admin.ezpark.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.admin.ezpark.R
import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.databinding.ItemDashboardCardBinding
import com.admin.ezpark.databinding.ItemDashboardCardType2Binding
import com.admin.ezpark.databinding.ItemDashboardCardType3Binding
import com.admin.ezpark.databinding.ItemDashboardType2ParentBinding
import com.admin.ezpark.enums.DashboardType


// Parent Adapter
class ParentDashboardAdapter(val callback: (DashboardCard) -> Unit) : RecyclerView.Adapter<ParentDashboardAdapter.ItemViewHolder>() {
    private val items: MutableList<Pair<String, List<DashboardCard>>> = mutableListOf()

    inner class ItemViewHolder(private val binding: ItemDashboardType2ParentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, itemList: List<DashboardCard>) {
            binding.titleTextView.text = title

            val itemAdapter = DashboardAdapter(callback)
            binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter = itemAdapter
            itemAdapter.submitList(itemList)
        }

    }

    fun submitList(newList: List<Pair<String, List<DashboardCard>>>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemDashboardType2ParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val (title, itemList) = items[position]
        holder.bind(title, itemList)
    }

    override fun getItemCount() = items.size
}


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

            DashboardType.TYPE3.type -> {
                val binding = ItemDashboardCardType3Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Type3ViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is Type1ViewHolder -> holder.bind(item, callback)
            is Type2ViewHolder -> holder.bind(item, callback)
            is Type3ViewHolder -> holder.bind(item, callback)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    class DashboardDiffCallback : DiffUtil.ItemCallback<DashboardCard>() {
        override fun areItemsTheSame(oldItem: DashboardCard, newItem: DashboardCard): Boolean {
            return oldItem.number == newItem.number && oldItem.title == newItem.title && oldItem.heading == newItem.heading
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
                titleTextView.text = item.title?.toFormattedString()
                iconImageView.setImageResource(item.imageResource ?: R.drawable.card_icon_placeholder)
                root.setOnClickListener { callback(item) }
            }
        }
    }


    class Type3ViewHolder(
        private val binding: ItemDashboardCardType3Binding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardCard, callback: (DashboardCard) -> Unit) {
            binding.apply {
                parkingTitle.text = item.heading ?: "JerryAxe"
                parkingImage.setImageResource(item.imageResource ?: R.drawable.card_icon_placeholder)
                parkingHours.text = item.hours
                root.setOnClickListener { callback(item) }
            }
        }
    }
}
