package com.emplk.realestatemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.DetailFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val binding by viewBinding { DetailFragmentBinding.bind(it) }
    private val viewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_ESTATE_ID = "EXTRA_ESTATE_ID"
        fun newInstance(id: Long): DetailFragment {
            val args = Bundle()
            args.putLong(EXTRA_ESTATE_ID, id)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { detailViewState ->
            binding.detailManagerName.text = detailViewState.agentName
        }
    }
}