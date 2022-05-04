package com.auric.submissionaplikasistoryapp.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.auric.submissionaplikasistoryapp.databinding.ItemRowStoryBinding
import com.auric.submissionaplikasistoryapp.model.ListStoryItem
import com.bumptech.glide.Glide

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.MyViewHolder>() {

    private var oldStoryItem = emptyList<ListStoryItem>()

    fun setData(newStoryItem: List<ListStoryItem>) {
        val diffUtil = StoryDiffUtil(oldStoryItem, newStoryItem)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldStoryItem = newStoryItem
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(var binding: ItemRowStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listStoryItem: ListStoryItem) {
            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.photo, "profile"),
                        Pair(binding.name, "name"),
                        Pair(binding.desc, "description"),
                    )
                intent.putExtra("ListStoryItem", listStoryItem)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
            binding.apply {
                Glide.with(itemView)
                    .load(listStoryItem.photoUrl)
                    .into(binding.photo)
                binding.name.text = listStoryItem.name
                binding.desc.text = listStoryItem.description
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(oldStoryItem[position])
    }

    override fun getItemCount(): Int = oldStoryItem.size

}