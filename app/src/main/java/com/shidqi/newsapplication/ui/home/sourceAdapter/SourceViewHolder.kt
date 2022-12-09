package com.shidqi.newsapplication.ui.home.sourceAdapter

import androidx.recyclerview.widget.RecyclerView
import com.shidqi.newsapplication.databinding.SourceLayoutItemBinding
import com.shidqi.newsapplication.models.SourceData

class SourceViewHolder(
    private val binding: SourceLayoutItemBinding,
    val onClick : (data:SourceData) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data : SourceData){
        binding.root.setOnClickListener {
            onClick(data)
        }
        binding.tvSourceId.text = data.name
    }
}