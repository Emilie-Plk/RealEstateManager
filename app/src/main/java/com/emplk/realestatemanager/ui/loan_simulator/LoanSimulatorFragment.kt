package com.emplk.realestatemanager.ui.loan_simulator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
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

        binding.resetButton.setOnClickListener {
            viewModel.onResetClicked()
        }

        binding.calculateButton.setOnClickListener {
            viewModel.onCalculateClicked()
        }

        binding.exitButtonIv.setOnClickListener {
            dismiss()
        }

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            val currentAmount = binding.amountEditText.text.toString()
            if (currentAmount != viewState.loanAmount) {
                binding.amountEditText.setText(viewState.loanAmount)
            }

            val currentRate = binding.interestRateEditText.text.toString()
            if (currentRate != viewState.loanRate) {
                binding.interestRateEditText.setText(viewState.loanRate)
            }

            val currentDuration = binding.loanDurationEditText.text.toString()
            if (currentDuration != viewState.loanDuration) {
                binding.loanDurationEditText.setText(viewState.loanDuration)
            }

            binding.monthlyPaymentTv.text = viewState.yearlyAndMonthlyPayment?.toCharSequence(view.context) ?: ""
        }

        binding.amountEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().length == 1 && text.toString().trim() == "0") {
                binding.amountEditText.setText("");
            }
        }
        binding.amountEditText.doAfterTextChanged {
            viewModel.onLoanAmountChanged(it?.toString() ?: "")
        }

        binding.interestRateEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().length == 1 && text.toString().trim() == "0") {
                binding.interestRateEditText.setText("");
            }
        }
        binding.interestRateEditText.doAfterTextChanged {
            viewModel.onInterestRateChanged(it?.toString() ?: "")
        }

        binding.loanDurationEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().length == 1 && text.toString().trim() == "0") {
                binding.loanDurationEditText.setText("");
            }
        }
        binding.loanDurationEditText.doAfterTextChanged {
            viewModel.onLoanDurationChanged(it?.toString() ?: "")
        }
    }
}