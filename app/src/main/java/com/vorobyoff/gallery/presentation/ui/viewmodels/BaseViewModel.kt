package com.vorobyoff.gallery.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.vorobyoff.gallery.base.Repository
import com.vorobyoff.gallery.data.RepositoryImpl
import com.vorobyoff.gallery.data.datasources.NetworkDataSource
import com.vorobyoff.gallery.data.networking.NetworkFactory.UNSPLASH_API

abstract class BaseViewModel : ViewModel() {
    protected val repository: Repository = RepositoryImpl(NetworkDataSource(UNSPLASH_API))
}