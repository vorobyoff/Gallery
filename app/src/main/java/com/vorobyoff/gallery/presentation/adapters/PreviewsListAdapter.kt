package com.vorobyoff.gallery.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.transform.RoundedCornersTransformation
import com.vorobyoff.gallery.databinding.ItemPreviewBinding
import com.vorobyoff.gallery.databinding.ItemPreviewBinding.inflate
import com.vorobyoff.gallery.presentation.adapters.PreviewsListAdapter.PreviewHolder
import com.vorobyoff.gallery.presentation.extension.loadImage
import com.vorobyoff.gallery.presentation.models.PreviewItem
import android.view.LayoutInflater.from as inflaterFrom

class PreviewsListAdapter : ListAdapter<PreviewItem, PreviewHolder>(ITEM_CALLBACK) {
    private lateinit var binding: ItemPreviewBinding

    companion object {
        val ITEM_CALLBACK: ItemCallback<PreviewItem> = object : ItemCallback<PreviewItem>() {
            override fun areItemsTheSame(old: PreviewItem, new: PreviewItem): Boolean =
                old.id == new.id

            override fun areContentsTheSame(old: PreviewItem, new: PreviewItem): Boolean =
                old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewHolder {
        binding = inflate(inflaterFrom(parent.context), parent, false)
        return PreviewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    class PreviewHolder(private val binding: ItemPreviewBinding) : ViewHolder(binding.root) {
        fun bind(item: PreviewItem) {
            binding.previewImg.loadImage(
                url = item.imgUrl,
                transforms = listOf(RoundedCornersTransformation(radius = 10f))
            )
        }
    }
}