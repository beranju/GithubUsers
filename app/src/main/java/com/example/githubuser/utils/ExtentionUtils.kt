package com.example.githubuser.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.google.android.material.snackbar.Snackbar

fun ImageView.loadImage(url: String?) =
    Glide.with(this.context)
        .load(url)
        .apply(
            RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
        )
        .centerCrop()
        .into(this)

fun View.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}