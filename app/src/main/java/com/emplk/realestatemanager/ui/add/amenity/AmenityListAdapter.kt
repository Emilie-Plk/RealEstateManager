package com.emplk.realestatemanager.ui.add.amenity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emplk.realestatemanager.databinding.AmenityCheckboxItemBinding

class AmenityListAdapter :
    ListAdapter<AmenityViewStateItem, AmenityListAdapter.AmenityViewHolder>(AmenityDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenityViewHolder =
        AmenityViewHolder.create(parent)

    override fun onBindViewHolder(holder: AmenityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AmenityViewHolder(private val binding: AmenityCheckboxItemBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup) = AmenityViewHolder(
                binding = AmenityCheckboxItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun bind(item: AmenityViewStateItem) {
            binding.addPropertyAmenitiesCheckbox.setText(item.stringRes)
       //     binding.addPropertyAmenitiesCheckbox.isChecked = item.isChecked
            binding.addPropertyAmenitiesCheckbox.setCompoundDrawablesWithIntrinsicBounds(item.iconDrawable, 0, 0, 0)
            binding.addPropertyAmenitiesCheckbox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = !isChecked
                //  item.onCheckBoxClicked.invoke()
            }
        }
    }
}

object AmenityDiffCallback : DiffUtil.ItemCallback<AmenityViewStateItem>() {
    override fun areItemsTheSame(oldItem: AmenityViewStateItem, newItem: AmenityViewStateItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AmenityViewStateItem, newItem: AmenityViewStateItem): Boolean =
        oldItem == newItem
}


