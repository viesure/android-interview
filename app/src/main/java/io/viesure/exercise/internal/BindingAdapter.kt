package io.viesure.exercise.internal

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .centerCrop()
        .into(imageView)
}

@BindingAdapter("title")
fun setTitle(textView: TextView, text: String?) {
    textView.text = text?.substring(0, 20)
}

@BindingAdapter("description")
fun setDescription(textView: TextView, text: String?) {
    textView.text = text?.substring(0, 45)
}