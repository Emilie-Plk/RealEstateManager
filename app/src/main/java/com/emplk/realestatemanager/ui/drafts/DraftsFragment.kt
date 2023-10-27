package com.emplk.realestatemanager.ui.drafts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.DraftsFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DraftsFragment : Fragment(R.layout.drafts_fragment) {

    private val binding by viewBinding { DraftsFragmentBinding.bind(it) }
    private val viewModel by viewModels<DraftsViewModel>()

    companion object {
        fun newInstance() = DraftsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val draftsAdapter = DraftsAdapter() // on déclare
        binding.draftsRv.adapter = draftsAdapter // on l'associe à la recyclerview

        viewModel.viewStates.observe(viewLifecycleOwner) { viewState ->
            draftsAdapter.submitList(viewState)
        }
    }
}