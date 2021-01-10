package com.vorobyoff.gallery.presentation.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vorobyoff.gallery.data.exceptions.catchNetworkException
import com.vorobyoff.gallery.presentation.models.PhotoItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewPhotoViewModel : BaseViewModel() {
    private val _photo = MutableLiveData<PhotoItem>()
    val photo: LiveData<PhotoItem> get() = _photo
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> get() = _error

    fun photo(id: String) {
        viewModelScope.launch {
            repository.photo(id)
                .catch { _error.value = catchNetworkException(it as Exception) }
                .collect { _photo.value = it }
        }
    }
}