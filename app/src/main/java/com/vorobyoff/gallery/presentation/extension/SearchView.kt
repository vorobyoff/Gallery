package com.vorobyoff.gallery.presentation.extension

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun SearchView.queryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            query.value = newText ?: ""
            return true
        }
    })

    return query
}