package com.shidqi.newsapplication.ui.home.sourceAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shidqi.newsapplication.databinding.SourceLayoutItemBinding
import com.shidqi.newsapplication.models.SourceData

class SourceAdapter(
    private val context: Context,
    private val dataset: MutableList<SourceData> = mutableListOf(),
    val onClick: (data: SourceData) -> Unit
) : RecyclerView.Adapter<SourceViewHolder>() {

    fun update(items:List<SourceData>){
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val binding = SourceLayoutItemBinding.inflate(
            LayoutInflater.from(context), parent,
            false
        )

        return SourceViewHolder(binding
            , onClick = onClick
        )
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {

        holder.bind(data = dataset[position])
    }

    override fun getItemCount(): Int = dataset.size
}