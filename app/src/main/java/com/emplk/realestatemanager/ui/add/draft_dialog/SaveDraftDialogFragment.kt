package com.emplk.realestatemanager.ui.add.draft_dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            binding.apply {
                addDraftDialogSaveButton.isVisible = viewState.isSaveButtonVisible
                addDraftDialogTitle.isVisible = viewState.isSaveButtonVisible
                addDraftDialogSaveButton.setOnClickListener {
                    viewState.saveButtonEvent()
                }

                addDraftDialogSaveTitle.isVisible = viewState.isSubmitTitleButtonVisible
                addDraftDialogSaveTitle.setOnClickListener {
                    viewState.submitTitleEvent(addDraftDialogTitleTextInputEditText.text?.toString() ?: "")
                }

                addDraftDialogDiscardButton.setOnClickListener {
                    viewState.discardEvent()
                }

                addDraftDialogTitleTextInputLayout.isVisible = viewState.isTitleTextInputVisible
            }
        }
    }
}