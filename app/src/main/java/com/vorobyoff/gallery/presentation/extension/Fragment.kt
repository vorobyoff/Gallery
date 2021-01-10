package com.vorobyoff.gallery.presentation.extension

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.vorobyoff.gallery.presentation.ui.delegate.FragmentBindingDelegate

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T): FragmentBindingDelegate<T> =
    FragmentBindingDelegate(this, viewBindingFactory)