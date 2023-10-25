package com.emplk.realestatemanager.ui.drafts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emplk.realestatemanager.databinding.FormDraftItemBinding

class DraftsAdapter : ListAdapter<DraftViewState, DraftsAdapter.DraftViewHolder>(DraftViewStateDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder =
        DraftViewHolder(
            FormDraftItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) = holder.bind(getItem(position))

    class DraftViewHolder(val binding: FormDraftItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DraftViewState) {
            binding.draftItemTitleTv.text = item.title
            binding.draftItemDateTv.text = item.lastEditionDate
            binding.root.setOnClickListener {
                item.onClick.invoke(item.id)
            }
        }
    }
}

object DraftViewStateDiffCallback : DiffUtil.ItemCallback<DraftViewState>() {
    override fun areItemsTheSame(oldItem: DraftViewState, newItem: DraftViewState): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DraftViewState, newItem: DraftViewState): Boolean {
        return oldItem == newItem
    }
}