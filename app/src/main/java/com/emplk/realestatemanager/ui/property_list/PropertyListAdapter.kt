package com.emplk.realestatemanager.ui.property_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emplk.realestatemanager.databinding.PropertyEmptyBinding
import com.emplk.realestatemanager.databinding.PropertyItemBinding

class PropertyListAdapter :
    ListAdapter<PropertyViewStateItem, PropertyListAdapter.PropertyViewHolder>(PropertyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder =
        when (PropertyViewStateItem.Type.values()[viewType]) {
            PropertyViewStateItem.Type.PROPERTY -> PropertyViewHolder.Property.create(parent)
            PropertyViewStateItem.Type.EMPTY_STATE -> PropertyViewHolder.EmptyState.create(parent)
        }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        when (holder) {
            is PropertyViewHolder.Property -> holder.bind(item = getItem(position) as PropertyViewStateItem.Property)
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

            fun bind(item: PropertyViewStateItem.Property) {
                binding.root.setOnClickListener { item.onClickEvent.invoke() }
                binding.propertyItemTypeTextView.text = item.typeOfProperty
                binding.propertyItemLocationTextView.text = item.location
                binding.propertyItemPriceTextView.text = item.price
                Glide.with(binding.propertyItemImageView)
                    .load(item.featuredPicture)
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

    object PropertyDiffCallback : DiffUtil.ItemCallback<PropertyViewStateItem>() {
        override fun areItemsTheSame(
            oldItem: PropertyViewStateItem,
            newItem: PropertyViewStateItem
        ): Boolean =
            when {
                oldItem is PropertyViewStateItem.Property &&
                        newItem is PropertyViewStateItem.Property ->
                    oldItem.id == newItem.id

                oldItem is PropertyViewStateItem.EmptyState &&
                        newItem is PropertyViewStateItem.EmptyState ->
                    true

                else -> false
            }

        override fun areContentsTheSame(
            oldItem: PropertyViewStateItem,
            newItem: PropertyViewStateItem
        ): Boolean = oldItem == newItem
    }
}