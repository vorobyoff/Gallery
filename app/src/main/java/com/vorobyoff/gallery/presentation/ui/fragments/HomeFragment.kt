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
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import coil.transform.CircleCropTransformation
import com.vorobyoff.gallery.R
import com.vorobyoff.gallery.data.exceptions.ConnectionException
import com.vorobyoff.gallery.data.exceptions.NetworkErrorException
import com.vorobyoff.gallery.data.models.OrientationParam.PORTRAIT
import com.vorobyoff.gallery.databinding.FragmentHomeBinding
import com.vorobyoff.gallery.databinding.FragmentHomeBinding.bind
import com.vorobyoff.gallery.presentation.adapters.CollectionsPagingAdapter
import com.vorobyoff.gallery.presentation.extension.loadImage
import com.vorobyoff.gallery.presentation.extension.viewBinding
import com.vorobyoff.gallery.presentation.models.CollectionItem
import com.vorobyoff.gallery.presentation.models.PhotoItem
import com.vorobyoff.gallery.presentation.ui.AppActivity
import com.vorobyoff.gallery.presentation.ui.decorations.VerticalItemDecoration
import com.vorobyoff.gallery.presentation.ui.fragments.CollectionPhotosFragment.Companion.COLLECTION_ID_KEY
import com.vorobyoff.gallery.presentation.ui.fragments.ViewPhotoFragment.Companion.PHOTO_ID_KEY
import com.vorobyoff.gallery.presentation.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    companion object {
        private const val PAGE_SIZE = 20
    }

    private val binding: FragmentHomeBinding by viewBinding { bind(it) }
    private val homeViewModel: HomeViewModel by viewModels()

    private val collectionsPagingAdapter: CollectionsPagingAdapter by lazy {
        CollectionsPagingAdapter {
            (requireActivity() as AppActivity).showFragment(
                fragmentClass = CollectionPhotosFragment::class.java,
                args = bundleOf(COLLECTION_ID_KEY to it.id)
            )
        }.apply { stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY }
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        fetchData()
    }

    private fun fetchData(): Unit = with(homeViewModel) {
        listCollections(PAGE_SIZE)
        randomPhoto(PORTRAIT)
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        customizeList()
        customizeAppbar()
        observeLiveData()
    }

    private fun customizeList(): Unit = with(binding.collectionsList) {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        addItemDecoration(VerticalItemDecoration(top = 10, bottom = 10))
        adapter = collectionsPagingAdapter
    }

    private fun customizeAppbar(): Unit = with(binding) {
        toolbar.setOnMenuItemClickListener {
            (requireActivity() as AppActivity).showFragment(fragmentClass = SearchPhotosFragment::class.java)
            true
        }

        randomImg.setOnClickListener {
            (requireActivity() as AppActivity).showFragment(
                fragmentClass = ViewPhotoFragment::class.java,
                args = bundleOf(PHOTO_ID_KEY to homeViewModel.randomPhoto.value?.id)
            )
        }
    }

    private fun observeLiveData(): Unit = with(homeViewModel) {
        listCollections.observe(viewLifecycleOwner) { onCollectionsReceived(it) }
        randomPhoto.observe(viewLifecycleOwner) { onRandomPhotoReceived(it) }
        error.observe(viewLifecycleOwner) { onErrorReceived(it) }
    }

    private fun onCollectionsReceived(data: PagingData<CollectionItem>) {
        lifecycleScope.launch {
            collectionsPagingAdapter.submitData(data)
        }
    }

    private fun onRandomPhotoReceived(photo: PhotoItem): Unit = with(binding) {
        profileImg.loadImage(
            url = photo.profileImg,
            transforms = listOf(CircleCropTransformation())
        )
        randomImg.loadImage(url = photo.url)
        usernameTxt.text = photo.username
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
            binding.error.root.visibility = GONE
            fetchData()
        }
    }
}