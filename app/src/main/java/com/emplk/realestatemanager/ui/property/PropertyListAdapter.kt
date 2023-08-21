package com.emplk.realestatemanager.ui.property

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emplk.realestatemanager.databinding.PropertyItemBinding

class PropertyListAdapter :
    ListAdapter<PropertyViewStateItem, PropertyListAdapter.PropertyViewHolder>(PropertyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder =
        PropertyViewHolder.Property(
            PropertyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        when (holder) {
            is PropertyViewHolder.Property -> holder.bind(item = getItem(position))
        }
    }
}

sealed class PropertyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class Property(private val binding: PropertyItemBinding) :
        PropertyViewHolder(binding.root) {
        fun bind(item: PropertyViewStateItem) {
            binding.propertyItemTypeTextView.text = item.type
            binding.propertyItemLocationTextView.text = item.location
            binding.propertyItemPriceTextView.text = item.price
            Glide.with(binding.propertyItemImageView)
                .load(item.featuredPicture)
                .into(binding.propertyItemImageView)
        }
    }
}

object PropertyDiffCallback : DiffUtil.ItemCallback<PropertyViewStateItem>() {
    override fun areItemsTheSame(
        oldItem: PropertyViewStateItem,
        newItem: PropertyViewStateItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PropertyViewStateItem,
        newItem: PropertyViewStateItem
    ): Boolean =
        oldItem == newItem
}
