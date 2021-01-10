package com.vorobyoff.gallery.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.vorobyoff.gallery.R
import com.vorobyoff.gallery.data.exceptions.ConnectionException
import com.vorobyoff.gallery.data.exceptions.NetworkErrorException
import com.vorobyoff.gallery.databinding.FragmentCollectionPhotosBinding
import com.vorobyoff.gallery.databinding.FragmentCollectionPhotosBinding.bind
import com.vorobyoff.gallery.presentation.adapters.PhotosPagingAdapter
import com.vorobyoff.gallery.presentation.extension.viewBinding
import com.vorobyoff.gallery.presentation.models.PhotoItem
import com.vorobyoff.gallery.presentation.ui.AppActivity
import com.vorobyoff.gallery.presentation.ui.decorations.VerticalItemDecoration
import com.vorobyoff.gallery.presentation.ui.fragments.ViewPhotoFragment.Companion.PHOTO_ID_KEY
import com.vorobyoff.gallery.presentation.ui.viewmodels.CollectionPhotosViewModel
import kotlinx.coroutines.launch

class CollectionPhotosFragment : Fragment(R.layout.fragment_collection_photos) {
    companion object {
        const val COLLECTION_ID_KEY = "collection_id"
        private const val PAGE_SIZE = 20
    }

    private val binding: FragmentCollectionPhotosBinding by viewBinding { bind(it) }
    private val collectionPhotosViewModel: CollectionPhotosViewModel by viewModels()

    private val photosPagingAdapter: PhotosPagingAdapter by lazy {
        PhotosPagingAdapter {
            (requireActivity() as AppActivity).replaceFragmentAndSave(
                fragmentClass = ViewPhotoFragment::class.java,
                args = bundleOf(PHOTO_ID_KEY to it.id)
            )
        }.apply { stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY }
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        arguments?.let { fetchData(it.getString(COLLECTION_ID_KEY)!!) }
    }

    private fun fetchData(collectionId: String): Unit =
        collectionPhotosViewModel.collectionPhotos(PAGE_SIZE, collectionId)

    override fun onViewCreated(view: View, state: Bundle?) {
        observeLiveData()
        customizeToolbar()
        customizeList()
    }

    private fun observeLiveData(): Unit = with(collectionPhotosViewModel) {
        collectionPhotos.observe(viewLifecycleOwner) { onPhotosReceived(it) }
        error.observe(viewLifecycleOwner) { onErrorReceived(it) }
    }

    private fun onPhotosReceived(data: PagingData<PhotoItem>) {
        lifecycleScope.launch { photosPagingAdapter.submitData(data) }
    }

    private fun onErrorReceived(exception: Exception): Unit = with(binding.error) {
        root.visibility = VISIBLE
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
            root.visibility = GONE
            arguments?.let { fetchData(it.getString(COLLECTION_ID_KEY)!!) }
        }
    }

    private fun customizeToolbar(): Unit = with(binding.toolbar) {
        setNavigationOnClickListener { (requireActivity() as AppActivity).comeback() }
        setOnMenuItemClickListener {
            (requireActivity() as AppActivity).replaceFragmentAndSave(SearchPhotosFragment::class.java)
            true
        }
    }

    private fun customizeList(): Unit = with(binding.photosList) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(VerticalItemDecoration(top = 10, bottom = 10))
        adapter = photosPagingAdapter
        setHasFixedSize(true)
    }
}