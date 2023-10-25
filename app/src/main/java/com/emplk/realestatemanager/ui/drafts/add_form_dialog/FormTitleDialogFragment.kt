package com.emplk.realestatemanager.ui.drafts.add_form_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FormTitleDialogFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormTitleDialogFragment : DialogFragment(R.layout.form_title_dialog_fragment) {

    companion object {
        fun newInstance() = FormTitleDialogFragment()
    }

    private val binding by viewBinding { FormTitleDialogFragmentBinding.bind(it) }
    private val viewModel by viewModels<FormTitleDialogFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNewFormSubmitBtn.setOnClickListener {
            viewModel.getEnteredTitle(binding.addNewFormTitleTextInputEditText.text.toString())
        }
    }

}