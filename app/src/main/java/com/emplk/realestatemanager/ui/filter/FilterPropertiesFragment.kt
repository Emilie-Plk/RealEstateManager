package com.emplk.realestatemanager.ui.filter

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FilterPropertiesFragmentBinding
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionListAdapter
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterPropertiesFragment : DialogFragment(R.layout.filter_properties_fragment) {
    companion object {
        fun newInstance() = FilterPropertiesFragment()
    }

    private val binding by viewBinding { FilterPropertiesFragmentBinding.bind(it) }
    private val viewModel by viewModels<FilterPropertiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val width = (Resources.getSystem().displayMetrics.widthPixels * 0.98).toInt()
        // for height I just want to wrap content
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)

        val predictionsAdapter = PredictionListAdapter()
        binding.filterAddressPredictionsRecyclerView.adapter = predictionsAdapter // why is it nullable??

    }
}