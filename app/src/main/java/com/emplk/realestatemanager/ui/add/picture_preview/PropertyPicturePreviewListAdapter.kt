package com.emplk.realestatemanager.ui.add.picture_preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.PropertyPreviewPictureItemBinding

class PropertyPicturePreviewListAdapter :
    ListAdapter<PicturePreviewStateItem, PropertyPicturePreviewListAdapter.PropertyPicturePreviewViewHolder>(
        PropertyPicturePreviewDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyPicturePreviewViewHolder =
        when (PicturePreviewStateItem.Type.values()[viewType]) {
            PicturePreviewStateItem.Type.ADD_PICTURE_PREVIEW ->
                PropertyPicturePreviewViewHolder.AddPropertyPicturePreview.create(parent)

            PicturePreviewStateItem.Type.EDIT_PICTURE_PREVIEW ->
                PropertyPicturePreviewViewHolder.EditPropertyPicturePreview.create(parent)
        }

    override fun onBindViewHolder(
        holder: PropertyPicturePreviewViewHolder,
        position: Int
    ) {
        when (holder) {
            is PropertyPicturePreviewViewHolder.AddPropertyPicturePreview -> holder.bind(
                item = getItem(position) as PicturePreviewStateItem.AddPropertyPicturePreview
            )

            is PropertyPicturePreviewViewHolder.EditPropertyPicturePreview -> holder.bind(
                item = getItem(position) as PicturePreviewStateItem.EditPropertyPicturePreview
            )
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    sealed class PropertyPicturePreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class AddPropertyPicturePreview(private val binding: PropertyPreviewPictureItemBinding) :
            PropertyPicturePreviewViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = AddPropertyPicturePreview(
                    binding = PropertyPreviewPictureItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: PicturePreviewStateItem.AddPropertyPicturePreview) {
                Glide
                    .with(itemView.context)
                    .load(item.uri)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .error(R.drawable.baseline_villa_24)
                    .into(binding.previewPictureIv)

                binding.previewPictureStarFeaturedIv.setOnClickListener {
                    item.onFeaturedEvent.invoke(!item.isFeatured)
                }

                binding.previewPictureStarFeaturedIv.setImageResource(
                    if (item.isFeatured) R.drawable.baseline_star_24
                    else R.drawable.baseline_star_border_24
                )

                binding.previewPictureDeleteIv.setOnClickListener { item.onDeleteEvent.invoke() }

                binding.previewPictureTitleEt.doAfterTextChanged { editable ->
                    item.onDescriptionChanged.invoke(editable.toString())
                }
            }
        }

        class EditPropertyPicturePreview(private val binding: PropertyPreviewPictureItemBinding) :
            PropertyPicturePreviewViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = EditPropertyPicturePreview(
                    binding = PropertyPreviewPictureItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: PicturePreviewStateItem.EditPropertyPicturePreview) {
                Glide
                    .with(itemView.context)
                    .load(item.uri)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .error(R.drawable.baseline_villa_24)
                    .into(binding.previewPictureIv)

                binding.previewPictureStarFeaturedIv.setImageResource(
                    if (item.isFeatured) R.drawable.baseline_star_24
                    else R.drawable.baseline_star_border_24
                )

                binding.previewPictureDeleteIv.setOnClickListener { item.onDeleteEvent.invoke() }

                binding.previewPictureStarFeaturedIv.setOnClickListener {
                    item.onFeaturedEvent.invoke(!item.isFeatured) // TODO: noooot sure at all lol
                }
                binding.previewPictureTitleEt.setText(item.description)

                binding.previewPictureTitleEt.doAfterTextChanged {
                    item.onDescriptionChanged.invoke(it.toString())
                }
            }
        }
    }
}

object PropertyPicturePreviewDiffCallback : DiffUtil.ItemCallback<PicturePreviewStateItem>() {
    override fun areItemsTheSame(
        oldItem: PicturePreviewStateItem,
        newItem: PicturePreviewStateItem
    ): Boolean =
        when {
            oldItem is PicturePreviewStateItem.AddPropertyPicturePreview &&
                    newItem is PicturePreviewStateItem.AddPropertyPicturePreview ->
                oldItem.uri == newItem.uri && oldItem.description == newItem.description &&
                        oldItem.isFeatured == newItem.isFeatured

            oldItem is PicturePreviewStateItem.EditPropertyPicturePreview &&
                    newItem is PicturePreviewStateItem.EditPropertyPicturePreview ->
                oldItem.uri == newItem.uri &&
                        oldItem.description == newItem.description &&
                        oldItem.isFeatured == newItem.isFeatured

            else -> false
        }

    override fun areContentsTheSame(
        oldItem: PicturePreviewStateItem,
        newItem: PicturePreviewStateItem
    ): Boolean = oldItem == newItem
}

