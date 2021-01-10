package com.vorobyoff.gallery.presentation.ui.delegate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment, private val bindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) fragment.viewLifecycleOwnerLiveData.observe(
                fragment
            ) {
                it.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_DESTROY) binding = null
                })
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding: T? = binding
        if (binding != null) return binding
        val lifecycle: Lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
            throw IllegalStateException("Should not initialize binding when fragment view are destroyed")

        return bindingFactory.invoke(thisRef.requireView())
            .also { this@FragmentBindingDelegate.binding = it }
    }
}