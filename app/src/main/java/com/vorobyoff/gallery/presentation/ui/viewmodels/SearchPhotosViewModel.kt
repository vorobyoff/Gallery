package com.vorobyoff.gallery.presentation.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vorobyoff.gallery.data.exceptions.catchNetworkException
import com.vorobyoff.gallery.presentation.models.PhotoItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchPhotosViewModel : BaseViewModel() {
    private val _photos = MutableLiveData<PagingData<PhotoItem>>()
    val photos: LiveData<PagingData<PhotoItem>> get() = _photos
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> get() = _error

    fun searchPhotos(pageSize: Int, query: String) {
        viewModelScope.launch {
            repository.searchPhotos(pageSize = pageSize, query = query)
                .cachedIn(this)
                .catch { _error.value = catchNetworkException(it as Exception) }
                .collectLatest { _photos.value = it }
        }
    }

}