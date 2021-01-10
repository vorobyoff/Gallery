package com.vorobyoff.gallery.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.request.Disposable
import coil.transform.CircleCropTransformation
import com.vorobyoff.gallery.databinding.ItemCollectionBinding
import com.vorobyoff.gallery.databinding.ItemCollectionBinding.inflate
import com.vorobyoff.gallery.presentation.adapters.CollectionsPagingAdapter.CollectionViewHolder
import com.vorobyoff.gallery.presentation.extension.loadImage
import com.vorobyoff.gallery.presentation.models.CollectionItem
import com.vorobyoff.gallery.presentation.models.PreviewItem
import com.vorobyoff.gallery.presentation.ui.decorations.HorizontalItemDecoration
import android.view.LayoutInflater.from as inflaterFrom

class CollectionsPagingAdapter(private val onItemClickListener: ((CollectionItem) -> Unit)? = null) :
    PagingDataAdapter<CollectionItem, CollectionViewHolder>(ITEM_CALLBACK) {
    private lateinit var binding: ItemCollectionBinding

    companion object {
        val ITEM_CALLBACK: ItemCallback<CollectionItem> = object : ItemCallback<CollectionItem>() {
            override fun areItemsTheSame(old: CollectionItem, new: CollectionItem): Boolean =
                old.id == new.id

            override fun areContentsTheSame(old: CollectionItem, new: CollectionItem): Boolean =
                old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        binding = inflate(inflaterFrom(parent.context), parent, false)
        return CollectionViewHolder(binding = binding, onItemClickListener = onItemClickListener)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    class CollectionViewHolder(
        private val binding: ItemCollectionBinding,
        private val onItemClickListener: ((CollectionItem) -> Unit)?
    ) : ViewHolder(binding.root) {
        private val recycledPool = RecycledViewPool()

        fun bind(item: CollectionItem?): Unit = item?.let {
            binding.titleTxt.text = it.title
            binding.usernameTxt.text = it.username
            createPreviewsList(it.previewItems)
            loadProfileImg(it.profileImg)
            setupListener(it)
        } ?: Unit

        private fun createPreviewsList(previews: List<PreviewItem>): Unit =
            with(binding.previewsList) {
                if (itemDecorationCount == 0) addItemDecoration(HorizontalItemDecoration(right = 30))
                layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
                setRecycledViewPool(recycledPool)
                setHasFixedSize(true)
                adapter = PreviewsListAdapter().apply {
                    stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    submitList(previews)
                }
            }

        private fun loadProfileImg(url: String): Disposable =
            binding.profileImg.loadImage(url = url, transforms = listOf(CircleCropTransformation()))

        private fun setupListener(collection: CollectionItem): Unit =
            onItemClickListener?.let { function ->
                binding.viewAllBtn.setOnClickListener { function.invoke(collection) }
            } ?: Unit
    }
}