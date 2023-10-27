package com.emplk.realestatemanager.ui.drafts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FormDraftItemBinding
import com.emplk.realestatemanager.ui.utils.NativePhoto.Companion.load

class DraftsAdapter : ListAdapter<DraftViewStateItem, DraftsAdapter.DraftViewHolder>(DraftViewStateDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder =
        when (DraftViewStateItem.Type.values()[viewType]) {
            DraftViewStateItem.Type.DRAFTS -> DraftViewHolder.Drafts.create(parent)
            DraftViewStateItem.Type.ADD_NEW_DRAFT -> DraftViewHolder.AddNewDraft.create(parent)
        }

    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) =
        when (holder) {
            is DraftViewHolder.Drafts -> holder.bind(item = getItem(position) as DraftViewStateItem.Drafts)
            is DraftViewHolder.AddNewDraft -> holder.bind(item = getItem(position) as DraftViewStateItem.AddNewDraft)
        }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    sealed class DraftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class Drafts(private val binding: FormDraftItemBinding) : DraftViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = Drafts(
                    binding = FormDraftItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: DraftViewStateItem.Drafts) {
                binding.draftItemTitleTv.text = item.title.toCharSequence(binding.root.context)
                binding.draftItemDateTv.text = item.lastEditionDate
                binding.root.setOnClickListener {
                    item.onClick.invoke()
                }
                item.featuredPicture.load(binding.draftItemPreviewIv)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .error(R.drawable.baseline_image_24)
                    .placeholder(R.drawable.baseline_image_24)
                    .into(binding.draftItemPreviewIv)
            }
        }

        class AddNewDraft(private val binding: FormDraftItemBinding) : DraftViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = AddNewDraft(
                    binding = FormDraftItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: DraftViewStateItem.AddNewDraft) {
                binding.draftItemTitleTv.text = "Add new draft"
                binding.root.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary_translucent
                    )
                )
                binding.root.strokeWidth = 1
                val layoutParams = binding.draftItemTitleTv.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

                binding.draftItemTitleTv.layoutParams = layoutParams
                binding.draftItemTitleTv.requestLayout()

                binding.root.strokeColor = ContextCompat.getColor(binding.root.context, R.color.secondary)
                binding.draftItemPreviewIv.setImageResource(R.drawable.baseline_add_home_24)
                binding.root.setOnClickListener {
                    item.onClick.invoke()
                }
            }
        }
    }
}

object DraftViewStateDiffCallback : DiffUtil.ItemCallback<DraftViewStateItem>() {
    override fun areItemsTheSame(oldItem: DraftViewStateItem, newItem: DraftViewStateItem): Boolean =
        when {
            oldItem is DraftViewStateItem.Drafts && newItem is DraftViewStateItem.Drafts -> oldItem.id == newItem.id
            oldItem is DraftViewStateItem.AddNewDraft && newItem is DraftViewStateItem.AddNewDraft -> oldItem.type == newItem.type
            else -> false
        }

    override fun areContentsTheSame(oldItem: DraftViewStateItem, newItem: DraftViewStateItem): Boolean =
        oldItem == newItem
}