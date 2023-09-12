package com.emplk.realestatemanager.ui.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentSpinnerAdapter
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeSpinnerAdapter
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }
    private val viewModel by viewModels<AddPropertyViewModel>()

    companion object {
        fun newInstance(): Fragment = AddPropertyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeAdapter = AddPropertyTypeSpinnerAdapter()
        binding.addPropertyTypeActv.setAdapter(typeAdapter)
        binding.addPropertyTypeActv.setOnItemClickListener { _, _, position, _ ->
            typeAdapter.getItem(position)?.let {
                viewModel.onPropertyTypeSelected(it.name)
            }
        }

        val agentAdapter = AddPropertyAgentSpinnerAdapter()
        binding.addPropertyAgentActv.setAdapter(agentAdapter)

        viewModel.addPropertyViewStateLiveData.observe(viewLifecycleOwner) {
        }

        binding.addPropertySubmitButton.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }
        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            typeAdapter.setData(viewState.propertyTypes)
            agentAdapter.setData(viewState.agents)
            binding.addPropertyPriceCurrencyTv.text = viewState.priceCurrency.toCharSequence(requireContext())
            binding.addPropertySurfaceUnitTv.text = viewState.surfaceUnit.toCharSequence(requireContext())
        //    binding.addPropertySubmitButton.isEnabled = viewState.isAddButtonEnabled
        }
    }
}

