package com.vorobyoff.gallery.presentation.extension

import android.widget.ImageView
import coil.load
import coil.request.Disposable
import coil.transform.Transformation

fun ImageView.loadImage(
    url: String,
    duration: Int = 200,
    crossfadeable: Boolean = true,
    transforms: List<Transformation>? = null
): Disposable = load(url) {
    transforms?.let { transformations(it) }
    crossfade(crossfadeable)
    crossfade(duration)
}