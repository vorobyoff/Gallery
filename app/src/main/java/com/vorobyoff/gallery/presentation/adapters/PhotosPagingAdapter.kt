package com.vorobyoff.gallery.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.transform.CircleCropTransformation
import com.vorobyoff.gallery.databinding.ItemPhotoBinding
import com.vorobyoff.gallery.databinding.ItemPhotoBinding.inflate
import com.vorobyoff.gallery.presentation.adapters.PhotosPagingAdapter.CollectionPhotoViewHolder
import com.vorobyoff.gallery.presentation.extension.loadImage
import com.vorobyoff.gallery.presentation.models.PhotoItem
import android.view.LayoutInflater.from as inflaterFrom

class PhotosPagingAdapter(private val onItemClickListener: ((PhotoItem) -> Unit)? = null) :
    PagingDataAdapter<PhotoItem, CollectionPhotoViewHolder>(ITEM_CALLBACK) {
    private lateinit var binding: ItemPhotoBinding

    companion object {
        val ITEM_CALLBACK: ItemCallback<PhotoItem> = object : ItemCallback<PhotoItem>() {
            override fun areItemsTheSame(old: PhotoItem, new: PhotoItem): Boolean = old.id == new.id

            override fun areContentsTheSame(old: PhotoItem, new: PhotoItem): Boolean = old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionPhotoViewHolder {
        binding = inflate(inflaterFrom(parent.context), parent, false)
        return CollectionPhotoViewHolder(
            binding = binding,
            onItemClickListener = onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: CollectionPhotoViewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    class CollectionPhotoViewHolder(
        private val binding: ItemPhotoBinding,
        private inline val onItemClickListener: ((PhotoItem) -> Unit)?
    ) : ViewHolder(binding.root) {

        fun bind(item: PhotoItem?): Unit = item?.let { photo ->
            with(binding) {
                profileImg.loadImage(
                    url = photo.profileImg,
                    transforms = listOf(CircleCropTransformation())
                )
                image.loadImage(photo.url)
                usernameTxt.text = photo.username
                onItemClickListener?.let { function ->
                    itemView.setOnClickListener { function.invoke(photo) }
                }
            }
        } ?: Unit
    }
}