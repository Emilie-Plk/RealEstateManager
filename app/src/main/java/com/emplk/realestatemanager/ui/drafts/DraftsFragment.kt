package com.emplk.realestatemanager.ui.drafts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emplk.realestatemanager.R


class DraftsFragment : Fragment(R.layout.drafts_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.drafts_fragment, container, false)
    }
}