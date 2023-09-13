package com.emplk.realestatemanager.ui.add

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentSpinnerAdapter
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeSpinnerAdapter
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import com.google.android.material.snackbar.Snackbar
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

        binding.addPropertyCreateButton.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }

        viewModel.viewEventLiveData.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is AddPropertyViewEvent.ShowSnackBarPropertyCreated -> {
                    Snackbar.make(
                        binding.root,
                        event.text.toCharSequence(requireContext()), Snackbar.LENGTH_LONG
                    ).show()
                }

                is AddPropertyViewEvent.OnAddPropertyClicked -> {

                }
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            typeAdapter.setData(viewState.propertyTypes)
            agentAdapter.setData(viewState.agents)
            binding.addPropertyPriceCurrencyTv.text = viewState.priceCurrency.toCharSequence(requireContext())
            binding.addPropertySurfaceUnitTv.text = viewState.surfaceUnit.toCharSequence(requireContext())
            //    binding.addPropertySubmitButton.isEnabled = viewState.isAddButtonEnabled
            binding.addPropertyProgressBar.isVisible = viewState.isProgressBarVisible
        }
    }
}

