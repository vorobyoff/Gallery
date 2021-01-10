package com.vorobyoff.gallery.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.vorobyoff.gallery.R
import com.vorobyoff.gallery.data.exceptions.ConnectionException
import com.vorobyoff.gallery.data.exceptions.NetworkErrorException
import com.vorobyoff.gallery.databinding.FragmentSearchPhotosBinding
import com.vorobyoff.gallery.databinding.FragmentSearchPhotosBinding.bind
import com.vorobyoff.gallery.presentation.adapters.PhotosPagingAdapter
import com.vorobyoff.gallery.presentation.extension.queryTextChangeStateFlow
import com.vorobyoff.gallery.presentation.extension.viewBinding
import com.vorobyoff.gallery.presentation.models.PhotoItem
import com.vorobyoff.gallery.presentation.ui.AppActivity
import com.vorobyoff.gallery.presentation.ui.decorations.VerticalItemDecoration
import com.vorobyoff.gallery.presentation.ui.fragments.ViewPhotoFragment.Companion.PHOTO_ID_KEY
import com.vorobyoff.gallery.presentation.ui.viewmodels.SearchPhotosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchPhotosFragment : Fragment(R.layout.fragment_search_photos) {
    companion object {
        private const val PAGE_SIZE = 10
    }

    private val binding: FragmentSearchPhotosBinding by viewBinding { bind(it) }
    private val findPhotoViewModel: SearchPhotosViewModel by viewModels()
    private val photosPagingAdapter by lazy {
        PhotosPagingAdapter {
            (requireActivity() as AppActivity).showFragment(
                fragmentClass = ViewPhotoFragment::class.java,
                args = bundleOf(PHOTO_ID_KEY to it.id)
            )
        }.apply { stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY }
    }

    @FlowPreview
    override fun onViewCreated(view: View, state: Bundle?) {
        observeLiveData()
        customizeSearch()
        customizeList()
    }

    private fun observeLiveData(): Unit = with(findPhotoViewModel) {
        photos.observe(viewLifecycleOwner) { onPhotosReceived(it) }
        error.observe(viewLifecycleOwner) { onErrorReceived(it) }
    }

    @FlowPreview
    private fun customizeSearch() {
        with(binding.photoSearch) {
            lifecycleScope.launch {
                queryTextChangeStateFlow()
                    .debounce(1500)
                    .flowOn(Dispatchers.IO)
                    .filter { return@filter it.isNotEmpty() }
                    .distinctUntilChanged()
                    .collect { fetchData(it.trim()) }
            }
        }
    }

    private fun fetchData(query: CharSequence): Unit =
        findPhotoViewModel.searchPhotos(PAGE_SIZE, "$query")

    private fun customizeList(): Unit = with(binding.photosList) {
        if (itemDecorationCount == 0) addItemDecoration(VerticalItemDecoration(top = 10, bottom = 10))
        layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        adapter = photosPagingAdapter
        setHasFixedSize(true)
    }

    private fun onPhotosReceived(data: PagingData<PhotoItem>) {
        lifecycleScope.launch { photosPagingAdapter.submitData(data) }
    }

    private fun onErrorReceived(exception: Exception): Unit = with(binding.error) {
        root.visibility = View.VISIBLE
        when (exception) {
            is ConnectionException -> errorDescriptionTxt.text =
                getString(R.string.connection_failed_error_msg)
            is NetworkErrorException -> {
                errorCodeTxt.text = "${exception.errorCode}"
                errorDescriptionTxt.text = exception.message
            }
            else -> errorDescriptionTxt.text = getString(R.string.unknown_error_msg)
        }

        retryBtn.setOnClickListener {
            root.visibility = View.GONE
            fetchData(binding.photoSearch.query)
        }
    }
}