package com.vorobyoff.gallery.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.vorobyoff.gallery.R
import com.vorobyoff.gallery.R.id.fragment_container
import com.vorobyoff.gallery.presentation.ui.fragments.HomeFragment

class AppActivity : FragmentActivity(R.layout.activity_app) {
    companion object {
        private const val SAVE_FRAGMENT_KEY = "save_fragment_key"
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        if (supportFragmentManager.backStackEntryCount == 0) replaceFragment(HomeFragment::class.java)
    }

    private fun replaceFragment(fragmentClass: Class<out Fragment>): Unit =
        supportFragmentManager.commitNow {
            replace(fragment_container, fragmentClass, null)
            setReorderingAllowed(true)
        }

    fun replaceFragmentAndSave(
        fragmentClass: Class<out Fragment>,
        tag: String = fragmentClass.simpleName,
        args: Bundle? = null
    ): Unit = supportFragmentManager.commit {
        replace(fragment_container, fragmentClass, args, tag)
        setReorderingAllowed(true)
        addToBackStack(tag)
    }

    fun showFragment(
        fragmentClass: Class<out Fragment>,
        tag: String = fragmentClass.simpleName,
        args: Bundle? = null
    ) {
        val showedFragment = supportFragmentManager.findFragmentById(fragment_container)!!
        supportFragmentManager.commit {
            add(fragment_container, fragmentClass, args, tag)
            show(fragmentClass.newInstance())
            setReorderingAllowed(true)
            hide(showedFragment)
            addToBackStack(tag)
        }
    }

    fun comeback(): Unit = supportFragmentManager.popBackStack()

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        val currentFragment = supportFragmentManager.findFragmentById(fragment_container)!!
        supportFragmentManager.putFragment(state, SAVE_FRAGMENT_KEY, currentFragment)
    }
}