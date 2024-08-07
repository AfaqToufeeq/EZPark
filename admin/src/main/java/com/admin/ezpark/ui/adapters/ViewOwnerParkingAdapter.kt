package com.admin.ezpark.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.admin.ezpark.R
import com.admin.ezpark.data.models.OwnerModel
import com.admin.ezpark.databinding.ItemViewOwnerParkingLayoutBinding
import com.bumptech.glide.Glide

class ViewOwnerParkingAdapter(private val callback: (OwnerModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val owners: MutableList<OwnerModel> = mutableListOf()
    private var ownersFiltered: MutableList<OwnerModel> = mutableListOf()

    init {
        ownersFiltered = owners
    }

    companion object {
        private const val TYPE_LAYOUT_1 = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LAYOUT_1 -> {
                val binding = ItemViewOwnerParkingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                Layout1ViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val owner = ownersFiltered[position]
        when (holder) {
            is Layout1ViewHolder -> holder.bind(owner)
        }
    }

    fun submitList(owners: List<OwnerModel>) {
        this.owners.clear()
        this.owners.addAll(owners)
        this.ownersFiltered.clear()
        this.ownersFiltered.addAll(owners)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = ownersFiltered.size

    override fun getItemViewType(position: Int): Int {
        return TYPE_LAYOUT_1
    }

    inner class Layout1ViewHolder(private val binding: ItemViewOwnerParkingLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(owner: OwnerModel) {
            Glide.with(binding.ivProfileImage)
                .load(owner.profileImageUrl)
                .placeholder(R.drawable.placeholder_person)
                .error(R.drawable.placeholder_person)
                .into(binding.ivProfileImage)

            binding.tvName.text = owner.ownerName
            binding.tvPhone.text = owner.phone
            binding.root.setOnClickListener { callback.invoke(owner) }
        }
    }

    // ... (Your existing adapter code)...

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                ownersFiltered = if (charString.isEmpty()) {
                    owners // Return the original complete list
                } else {
                    val filteredList = owners.filter { owner ->
                        owner.ownerName.lowercase().contains(charString.lowercase()) ||
                                owner.phone.contains(charString)
                    }
                    filteredList.toMutableList()
                }
                return FilterResults().apply { values = ownersFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ownersFiltered = if (results?.values == null)
                    mutableListOf()
                else
                    results.values as MutableList<OwnerModel>
                notifyDataSetChanged()
            }
        }
    }
}
