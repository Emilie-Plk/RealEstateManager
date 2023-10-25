package com.emplk.realestatemanager.ui.add.draft_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.SaveDraftDialogFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveDraftDialogFragment : DialogFragment(R.layout.save_draft_dialog_fragment) {

    companion object {
        fun newInstance() = SaveDraftDialogFragment()
    }

    private val binding by viewBinding { SaveDraftDialogFragmentBinding.bind(it) }
    private val viewModel by viewModels<SaveDraftDialogViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addDraftDialogTitleTextInputLayout.visibility = View.GONE
        binding.addDraftDialogTitleTextInputEditText.visibility = View.GONE
        binding.addDraftDialogSaveTitle.visibility = View.GONE

        binding.addDraftDialogSave.setOnClickListener {
            binding.addDraftDialogSaveTitle.visibility = View.VISIBLE
            binding.addDraftDialogTitleTextInputLayout.visibility = View.VISIBLE
            binding.addDraftDialogTitleTextInputEditText.visibility = View.VISIBLE

            binding.addDraftDialogTitle.visibility = View.GONE
            binding.addDraftDialogSave.visibility = View.GONE
            binding.addDraftDialogDiscard.visibility = View.GONE
        }

        binding.addDraftDialogSaveTitle.setOnClickListener {
            viewModel.getFormTitle(binding.addDraftDialogTitleTextInputEditText.text.toString())
            viewModel.onAddDraftClicked()
            dismiss()
        }

        binding.addDraftDialogDiscard.setOnClickListener {
            viewModel.onCancelClicked()
            dismiss()
        }
    }
}