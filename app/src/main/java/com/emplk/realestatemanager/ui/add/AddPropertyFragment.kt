package com.emplk.realestatemanager.ui.add

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }
    private val viewModel by viewModels<AddPropertyViewModel>()

    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addPropertyPicturesFromStorageButton.setOnClickListener {
            val imageView = ImageView(requireActivity())

            imageView.apply {
                imageView.setImageResource(R.drawable.tools_villa)
                imageView.layoutParams = ViewGroup.MarginLayoutParams(
                    500,
                    400
                ).apply {
                    setMargins(8, 8, 8, 8)
                }
                imageView.isClickable = true
                imageView.isFocusable = true
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.contentDescription = "A beautiful villa"
            }
            binding.addPropertyPicturesFlexboxLayout.addView(imageView)
        }
    }
}

