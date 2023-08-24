package com.emplk.realestatemanager.ui.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

sealed class NativePhoto {
    data class Uri(
        val uri: String,
    ) : NativePhoto()

    data class Resource(
        @DrawableRes
        val resource: Int,
    ) : NativePhoto()

    companion object {
        fun NativePhoto.load(imageView: ImageView): RequestBuilder<Drawable> = Glide.with(imageView).load(
            when (this) {
                is Uri -> this.uri
                is Resource -> this.resource
            }
        )
    }
}
