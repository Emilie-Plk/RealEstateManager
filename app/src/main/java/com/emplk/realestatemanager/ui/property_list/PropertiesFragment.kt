package com.emplk.realestatemanager.ui.property_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.PropertiesFragmentBinding
import com.emplk.realestatemanager.ui.detail.DetailActivity
import com.emplk.realestatemanager.ui.detail.DetailFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertiesFragment : Fragment(R.layout.properties_fragment) {

    private val binding by viewBinding { PropertiesFragmentBinding.bind(it) }
    private val viewModel by viewModels<PropertyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PropertyListAdapter()
        binding.propertiesRv.adapter = adapter

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            adapter.submitList(viewState)
        }

        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                is PropertyViewEvent.NavigateToDetailActivity ->
                    startActivity(
                        DetailActivity.navigate(requireContext(), event.id)
                    )

                is PropertyViewEvent.DisplayDetailFragment ->
                    parentFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_FrameLayout_container_detail, DetailFragment.newInstance(event.id)
                        )
                        .commitNow()
            }
        }
    }
}