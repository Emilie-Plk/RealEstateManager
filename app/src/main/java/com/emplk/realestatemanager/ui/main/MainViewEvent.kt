package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object DisplayPropertyListFragmentOnPhone : MainViewEvent()
    object DisplayPropertyListFragmentOnTablet : MainViewEvent()
    object DisplayAddPropertyFragmentOnPhone : MainViewEvent()
    object DisplayAddPropertyFragmentOnTablet : MainViewEvent()
    object DisplayEditPropertyFragmentOnPhone : MainViewEvent()
    object DisplayEditPropertyFragmentOnTablet : MainViewEvent()
    object DisplayBlankFragment : MainViewEvent()
    object DisplayDetailFragment : MainViewEvent()
    object StartDetailActivity : MainViewEvent()
}