package com.emplk.realestatemanager.ui.add.address_predictions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emplk.realestatemanager.databinding.PropertyAddressPredictionEmptyBinding
import com.emplk.realestatemanager.databinding.PropertyAddressPredictionItemBinding

class PredictionListAdapter :
    ListAdapter<PredictionViewState, PredictionListAdapter.PredictionViewHolder>(PredictionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder =
        when (PredictionViewState.Type.values()[viewType]) {
            PredictionViewState.Type.PREDICTION -> PredictionViewHolder.Prediction.create(parent)
            PredictionViewState.Type.EMPTY_STATE -> PredictionViewHolder.EmptyState.create(parent)
        }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        when (holder) {
            is PredictionViewHolder.Prediction -> holder.bind(item = getItem(position) as PredictionViewState.Prediction)
            is PredictionViewHolder.EmptyState -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    sealed class PredictionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class Prediction(private val binding: PropertyAddressPredictionItemBinding) :
            PredictionViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = Prediction(
                    binding = PropertyAddressPredictionItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            fun bind(item: PredictionViewState.Prediction) {
                binding.formSuggestionTextView.setOnClickListener {
                    item.onClickEvent.invoke(item.address)
                }
                binding.formSuggestionTextView.text = item.address
            }
        }

        class EmptyState(private val binding: PropertyAddressPredictionEmptyBinding) :
            PredictionViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = EmptyState(
                    binding = PropertyAddressPredictionEmptyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}

object PredictionDiffCallback : DiffUtil.ItemCallback<PredictionViewState>() {
    override fun areItemsTheSame(oldItem: PredictionViewState, newItem: PredictionViewState): Boolean =
        when {
            oldItem is PredictionViewState.Prediction && newItem is PredictionViewState.Prediction -> oldItem.address == newItem.address

            oldItem is PredictionViewState.EmptyState && newItem is PredictionViewState.EmptyState -> true

            else -> false
        }

    override fun areContentsTheSame(oldItem: PredictionViewState, newItem: PredictionViewState): Boolean =
        oldItem == newItem
}