package com.emplk.realestatemanager.ui.drafts.add_form_dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddNewFormFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewFormDialogFragment : DialogFragment(R.layout.add_new_form_fragment) {

    private val binding by viewBinding { AddNewFormFragmentBinding.bind(it) }
    private val viewModel by viewModels<AddNewFormDialogViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}