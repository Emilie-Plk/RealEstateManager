package com.emplk.realestatemanager.ui.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.LoadingStateBinding
import com.emplk.realestatemanager.databinding.PropertyEmptyBinding
import com.emplk.realestatemanager.databinding.PropertyItemBinding
import com.emplk.realestatemanager.ui.utils.NativePhoto.Companion.load

class PropertyListAdapter :
    ListAdapter<PropertiesViewState, PropertyListAdapter.PropertyViewHolder>(PropertyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder =
        when (PropertiesViewState.Type.values()[viewType]) {
            PropertiesViewState.Type.PROPERTIES -> PropertyViewHolder.Property.create(parent)
            PropertiesViewState.Type.LOADING -> PropertyViewHolder.LoadingState.create(parent)
            PropertiesViewState.Type.EMPTY_STATE -> PropertyViewHolder.EmptyState.create(parent)
        }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        when (holder) {
            is PropertyViewHolder.Property -> holder.bind(item = getItem(position) as PropertiesViewState.Properties)
            is PropertyViewHolder.LoadingState -> Unit
            is PropertyViewHolder.EmptyState -> holder.bind(item = getItem(position) as PropertiesViewState.EmptyState)
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

            fun bind(item: PropertiesViewState.Properties) {
                binding.root.setOnClickListener { item.onClickEvent.invoke() }
                binding.propertyItemTypeTextView.text = item.propertyType
                binding.propertyItemLocationTextView.text = item.address
                binding.propertyItemPriceTextView.text = item.price
                binding.propertyItemRoomTextView?.text = item.room.toCharSequence(binding.root.context)
                binding.propertyItemBathroomTextView?.text = item.bathroom.toCharSequence(binding.root.context)
                binding.propertyItemBedroomTextView?.text = item.bedroom.toCharSequence(binding.root.context)
                binding.propertyItemSurfaceTextView?.text = item.surface
                when (item.isSold) {
                    true -> {
                        binding.propertyItemSoldShade.isVisible = true
                        binding.propertyItemSoldBannerImageView.visibility = View.VISIBLE
                        binding.propertyItemSoldTextView.visibility = View.VISIBLE
                        binding.propertyItemPriceTextView.paintFlags = binding.propertyItemPriceTextView.paintFlags or
                                Paint.STRIKE_THRU_TEXT_FLAG
                    }

                    else -> {
                        binding.propertyItemSoldShade.isVisible = false
                        binding.propertyItemSoldBannerImageView.visibility = View.GONE
                        binding.propertyItemSoldTextView.visibility = View.GONE
                    }
                }
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

            fun bind(item: PropertiesViewState.EmptyState) {
                binding.emptyPropertyButtonAdd.setOnClickListener { item.onAddClick.invoke() }
            }
        }

        class LoadingState(private val binding: LoadingStateBinding) :
            PropertyViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = LoadingState(
                    binding = LoadingStateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}

object PropertyDiffCallback : DiffUtil.ItemCallback<PropertiesViewState>() {
    override fun areItemsTheSame(
        oldItem: PropertiesViewState,
        newItem: PropertiesViewState
    ): Boolean =
        when {
            oldItem is PropertiesViewState.Properties &&
                    newItem is PropertiesViewState.Properties ->
                oldItem.id == newItem.id

            oldItem is PropertiesViewState.LoadingState &&
                    newItem is PropertiesViewState.LoadingState ->
                true

            oldItem is PropertiesViewState.EmptyState &&
                    newItem is PropertiesViewState.EmptyState ->
                true

            else -> false
        }

    override fun areContentsTheSame(
        oldItem: PropertiesViewState,
        newItem: PropertiesViewState
    ): Boolean = oldItem == newItem
}
