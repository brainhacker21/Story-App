package com.auric.submissionaplikasistoryapp.story

import androidx.recyclerview.widget.DiffUtil
import com.auric.submissionaplikasistoryapp.model.ListStoryItem

class StoryDiffUtil(
    private val oldList: List<ListStoryItem>,
    private val newList: List<ListStoryItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}