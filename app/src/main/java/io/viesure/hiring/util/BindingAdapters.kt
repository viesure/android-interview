package io.viesure.hiring.util

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun setImageViewSrc(view: AppCompatImageView, uri: Uri?) {
    Glide.with(view.context)
        .load(uri)
        .into(view)
}

@BindingAdapter("circleImage")
fun setImageViewSrcCircleCropped(view: AppCompatImageView, uri: Uri?) {
    Glide.with(view.context)
        .load(uri)
        .circleCrop()
        .into(view)
}