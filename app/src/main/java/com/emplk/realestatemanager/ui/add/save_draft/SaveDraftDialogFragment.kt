package com.emplk.realestatemanager.ui.add.save_draft

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
                addDraftDialogTextView.isVisible = viewState.isSaveMessageVisible
                if (viewState.isSaveMessageVisible) {
                    addDraftDialogSaveButton.text = resources.getString(R.string.save_draft_btn_text)
                    addDraftDialogTextView.text = viewState.dialogMessage.toCharSequence(requireContext())
                    addDraftDialogSaveButton.setOnClickListener {
                        viewState.saveButtonEvent()
                    }
                } else {
                    addDraftDialogSaveButton.text = resources.getString(R.string.submit_title_btn_text)
                    addDraftDialogSaveButton.setOnClickListener {
                        viewState.submitTitleEvent(addDraftDialogTitleTextInputEditText.text?.toString() ?: "")
                        dismiss()
                    }
                }

                addDraftDialogDiscardButton.setOnClickListener {
                    viewState.discardEvent()
                }

                addDraftDialogTitleTextInputLayout.isVisible = viewState.isTitleTextInputVisible
            }
        }
    }
}