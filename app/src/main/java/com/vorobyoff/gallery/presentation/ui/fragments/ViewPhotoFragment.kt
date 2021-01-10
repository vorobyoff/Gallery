package com.vorobyoff.gallery.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vorobyoff.gallery.R
import com.vorobyoff.gallery.data.exceptions.ConnectionException
import com.vorobyoff.gallery.data.exceptions.NetworkErrorException
import com.vorobyoff.gallery.databinding.FragmentViewPhotoBinding
import com.vorobyoff.gallery.databinding.FragmentViewPhotoBinding.bind
import com.vorobyoff.gallery.presentation.extension.loadImage
import com.vorobyoff.gallery.presentation.extension.viewBinding
import com.vorobyoff.gallery.presentation.models.PhotoItem
import com.vorobyoff.gallery.presentation.ui.viewmodels.ViewPhotoViewModel

class ViewPhotoFragment : Fragment(R.layout.fragment_view_photo) {
    companion object {
        const val PHOTO_ID_KEY = "photo_id"
    }

    private val photoId: String by lazy { arguments?.getString(PHOTO_ID_KEY) ?: "" }
    private val binding: FragmentViewPhotoBinding by viewBinding { bind(it) }
    private val viewPhotoViewModel: ViewPhotoViewModel by viewModels()

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        fetchData()
    }

    private fun fetchData(): Unit = viewPhotoViewModel.photo(photoId)

    override fun onViewCreated(view: View, state: Bundle?): Unit = observeLiveData()

    private fun observeLiveData(): Unit = with(viewPhotoViewModel) {
        photo.observe(viewLifecycleOwner) { onPhotoReceived(it) }
        error.observe(viewLifecycleOwner) { onErrorReceived(it) }
    }

    private fun onPhotoReceived(photo: PhotoItem): Unit = with(binding) {
        image.loadImage(photo.url)
        nameTxt.text = photo.name
        linkTxt.text = photo.link
        descriptionTxt.text = photo.description ?: "Nothing to show :("
        sizeTxt.text = getString(R.string.size_pattern, photo.width, photo.height)
    }

    private fun onErrorReceived(exception: Exception): Unit = with(binding.error) {
        root.visibility = VISIBLE
        when (exception) {
            is ConnectionException -> errorDescriptionTxt.text =
                getString(R.string.connection_failed_error_msg)
            is NetworkErrorException -> {
                errorDescriptionTxt.text = "${exception.errorCode}"
                errorDescriptionTxt.text = exception.message
            }
            else -> errorDescriptionTxt.text = getString(R.string.unknown_error_msg)
        }

        retryBtn.setOnClickListener {
            root.visibility = GONE
            fetchData()
        }
    }
}