package com.emplk.realestatemanager.ui.detail.picture_banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.DetailPictureBannerBinding
import com.emplk.realestatemanager.ui.utils.NativePhoto.Companion.load

class PictureBannerListAdapter :
    ListAdapter<PictureBannerViewState, PictureBannerListAdapter.PictureViewHolder>(PictureDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder =
        PictureViewHolder.create(parent)

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) = holder.bind(getItem(position))

    class PictureViewHolder(private val binding: DetailPictureBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup) = PictureViewHolder(
                binding = DetailPictureBannerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun bind(item: PictureBannerViewState) {
            item.pictureUri.load(binding.ivBanner)
                .error(R.drawable.baseline_villa_24)
                .into(binding.ivBanner)

            binding.tvBanner.text = item.description
        }
    }
}

object PictureDiffCallback : DiffUtil.ItemCallback<PictureBannerViewState>() {
    override fun areItemsTheSame(oldItem: PictureBannerViewState, newItem: PictureBannerViewState): Boolean =
        oldItem.pictureUri == newItem.pictureUri && oldItem.description == newItem.description

    override fun areContentsTheSame(oldItem: PictureBannerViewState, newItem: PictureBannerViewState): Boolean =
        oldItem == newItem
}
