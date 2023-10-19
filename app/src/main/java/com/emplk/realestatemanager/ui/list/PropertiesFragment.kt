package com.emplk.realestatemanager.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.PropertiesFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertiesFragment : Fragment(R.layout.properties_fragment) {

    private val binding by viewBinding { PropertiesFragmentBinding.bind(it) }
    private val viewModel by viewModels<PropertiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PropertyListAdapter()
        binding.propertiesRv.adapter = adapter

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            adapter.submitList(viewState)
        }
    }

    companion object {
        fun newInstance() = PropertiesFragment()
    }
}