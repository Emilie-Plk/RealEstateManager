package com.emplk.realestatemanager.ui.detail.picture_banner

import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

data class PictureBannerViewState(
    val pictureUri: NativePhoto,
    val description: String?,
    val picturePosition: Int,
    val pictureNumberText: NativeText,
)