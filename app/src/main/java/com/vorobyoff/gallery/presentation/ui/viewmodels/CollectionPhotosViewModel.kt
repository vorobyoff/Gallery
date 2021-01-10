package com.vorobyoff.gallery.presentation.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vorobyoff.gallery.data.exceptions.catchNetworkException
import com.vorobyoff.gallery.presentation.models.PhotoItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CollectionPhotosViewModel : BaseViewModel() {
    private val _collectionPhotos = MutableLiveData<PagingData<PhotoItem>>()
    val collectionPhotos: LiveData<PagingData<PhotoItem>> get() = _collectionPhotos
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun collectionPhotos(pageSize: Int, id: String) {
        viewModelScope.launch {
            repository.collectionPhotos(pageSize = pageSize, id = id)
                .cachedIn(this)
                .catch { _error.value = catchNetworkException(it as Exception) }
                .collect { _collectionPhotos.value = it }
        }
    }
}