package com.emplk.realestatemanager.ui.add.add_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddDraftDialogFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDraftDialogFragment : DialogFragment(R.layout.add_draft_dialog_fragment) {

    companion object {
        fun newInstance() = AddDraftDialogFragment()
    }

    private val binding by viewBinding { AddDraftDialogFragmentBinding.bind(it) }
    private val viewModel by viewModels<AddDraftDialogViewModel>()

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