package com.vorobyoff.gallery.presentation.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vorobyoff.gallery.data.exceptions.catchNetworkException
import com.vorobyoff.gallery.data.models.OrientationParam
import com.vorobyoff.gallery.presentation.models.CollectionItem
import com.vorobyoff.gallery.presentation.models.PhotoItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    private val _randomPhoto = MutableLiveData<PhotoItem>()
    val randomPhoto: LiveData<PhotoItem> get() = _randomPhoto
    private val _listCollections = MutableLiveData<PagingData<CollectionItem>>()
    val listCollections: LiveData<PagingData<CollectionItem>> get() = _listCollections
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun randomPhoto(orientation: OrientationParam) {
        viewModelScope.launch {
            repository.randomPhoto(orientation)
                .catch { _error.value = catchNetworkException(it as Exception) }
                .collect { _randomPhoto.value = it }
        }
    }

    fun listCollections(pageSize: Int) {
        viewModelScope.launch {
            repository.listCollections(pageSize).cachedIn(this)
                .catch { _error.value = catchNetworkException(it as Exception) }
                .collect { _listCollections.value = it }
        }
    }
}