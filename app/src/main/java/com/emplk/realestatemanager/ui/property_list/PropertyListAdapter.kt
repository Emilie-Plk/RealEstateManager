package com.emplk.realestatemanager.ui.property_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.PropertyEmptyBinding
import com.emplk.realestatemanager.databinding.PropertyItemBinding
import com.emplk.realestatemanager.ui.utils.NativePhoto.Companion.load

class PropertyListAdapter :
    ListAdapter<PropertyViewState, PropertyListAdapter.PropertyViewHolder>(PropertyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder =
        when (PropertyViewState.Type.values()[viewType]) {
            PropertyViewState.Type.PROPERTY -> PropertyViewHolder.Property.create(parent)
            PropertyViewState.Type.EMPTY_STATE -> PropertyViewHolder.EmptyState.create(parent)
        }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        when (holder) {
            is PropertyViewHolder.Property -> holder.bind(item = getItem(position) as PropertyViewState.Property)
            is PropertyViewHolder.EmptyState -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    sealed class PropertyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class Property(private val binding: PropertyItemBinding) :
            PropertyViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = Property(
                    binding = PropertyItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: PropertyViewState.Property) {
                binding.root.setOnClickListener { item.onClickEvent.invoke() }
                binding.propertyItemTypeTextView.text = item.typeOfProperty
                binding.propertyItemLocationTextView.text = item.address
                binding.propertyItemPriceTextView.text = item.price
                item.featuredPicture
                    .load(binding.propertyItemImageView)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .error(R.drawable.baseline_villa_24)
                    .into(binding.propertyItemImageView)
            }
        }

        class EmptyState(private val binding: PropertyEmptyBinding) :
            PropertyViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = EmptyState(
                    binding = PropertyEmptyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    object PropertyDiffCallback : DiffUtil.ItemCallback<PropertyViewState>() {
        override fun areItemsTheSame(
            oldItem: PropertyViewState,
            newItem: PropertyViewState
        ): Boolean =
            when {
                oldItem is PropertyViewState.Property &&
                        newItem is PropertyViewState.Property ->
                    oldItem.id == newItem.id

                oldItem is PropertyViewState.EmptyState &&
                        newItem is PropertyViewState.EmptyState ->
                    true

                else -> false
            }

        override fun areContentsTheSame(
            oldItem: PropertyViewState,
            newItem: PropertyViewState
        ): Boolean = oldItem == newItem
    }
}