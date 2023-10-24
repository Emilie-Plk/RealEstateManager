package com.emplk.realestatemanager.ui.add.amenity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emplk.realestatemanager.databinding.AmenityCheckboxItemBinding
import com.emplk.realestatemanager.databinding.AmenityItemBinding

class AmenityListAdapter :
    ListAdapter<AmenityViewState, AmenityListAdapter.AmenityViewHolder>(AmenityDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenityViewHolder =
        when (AmenityViewState.Type.values()[viewType]) {
            AmenityViewState.Type.AMENITY_CHECKBOX -> AmenityViewHolder.AmenityCheckbox.create(parent)
            AmenityViewState.Type.AMENITY_ITEM -> AmenityViewHolder.AmenityItem.create(parent)
        }

    override fun onBindViewHolder(holder: AmenityViewHolder, position: Int) =
        when (holder) {
            is AmenityViewHolder.AmenityCheckbox -> holder.bind(item = getItem(position) as AmenityViewState.AmenityCheckbox)
            is AmenityViewHolder.AmenityItem -> holder.bind(item = getItem(position) as AmenityViewState.AmenityItem)
        }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    sealed class AmenityViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class AmenityCheckbox(private val binding: AmenityCheckboxItemBinding) :
            AmenityViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = AmenityCheckbox(
                    binding = AmenityCheckboxItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: AmenityViewState.AmenityCheckbox) {
                binding.formAmenitiesCheckbox.setText(item.stringRes)
                binding.formAmenitiesCheckbox.setCompoundDrawablesWithIntrinsicBounds(item.iconDrawable, 0, 0, 0)
                binding.formAmenitiesCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    item.onCheckBoxClicked.invoke(isChecked)
                }
                binding.formAmenitiesCheckbox.isChecked = item.isChecked
            }
        }

        class AmenityItem(private val binding: AmenityItemBinding) :
            AmenityViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = AmenityItem(
                    binding = AmenityItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: AmenityViewState.AmenityItem) {
                binding.detailAmenitiesTv.setText(item.stringRes)
                binding.detailAmenitiesTv.setCompoundDrawablesWithIntrinsicBounds(item.iconDrawable, 0, 0, 0)
            }
        }

    }
}

object AmenityDiffCallback : DiffUtil.ItemCallback<AmenityViewState>() {
    override fun areItemsTheSame(oldItem: AmenityViewState, newItem: AmenityViewState): Boolean =
        when {
            oldItem is AmenityViewState.AmenityCheckbox && newItem is AmenityViewState.AmenityCheckbox -> oldItem.id == newItem.id
            oldItem is AmenityViewState.AmenityItem && newItem is AmenityViewState.AmenityItem -> oldItem.stringRes == newItem.stringRes
            else -> false
        }

    override fun areContentsTheSame(oldItem: AmenityViewState, newItem: AmenityViewState): Boolean =
        oldItem == newItem
}


