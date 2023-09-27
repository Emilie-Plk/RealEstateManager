package com.emplk.realestatemanager.ui.add.draft_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddDraftDialogFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertyDraftDialogFragment : DialogFragment(R.layout.add_draft_dialog_fragment) {

    companion object {
        fun newInstance() = PropertyDraftDialogFragment()
    }

    private val binding by viewBinding { AddDraftDialogFragmentBinding.bind(it) }
    private val viewModel by viewModels<PropertyDraftDialogViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addDraftDialogSave.setOnClickListener {
            viewModel.onAddDraftClicked()
            dismiss()
        }

        binding.addDraftDialogDiscard.setOnClickListener {
            viewModel.onCancelClicked()
            dismiss()
        }
    }
}