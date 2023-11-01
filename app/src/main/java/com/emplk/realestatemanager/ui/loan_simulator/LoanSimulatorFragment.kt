package com.emplk.realestatemanager.ui.loan_simulator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.LoanSimulatorFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanSimulatorFragment : BottomSheetDialogFragment(R.layout.loan_simulator_fragment) {

    companion object {
        fun newInstance() = LoanSimulatorFragment()
    }


    private val binding by viewBinding { LoanSimulatorFragmentBinding.bind(it) }
    private val viewModel by viewModels<LoanSimulatorViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bottom sheet expanded
        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.loanSimulatorConstraintLayout as View)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
// set peek height to half screen
        standardBottomSheetBehavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
        standardBottomSheetBehavior.isDraggable = false


        binding.exitButtonIv.setOnClickListener {
            dismiss()
        }


    }
}