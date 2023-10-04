package com.emplk.realestatemanager.ui.detail.picture_banner

import com.emplk.realestatemanager.ui.utils.NativePhoto

data class PictureBannerViewState(
    val pictureUri: NativePhoto,
    val description: String,
    val isFeatured: Boolean,
)