package com.shidqi.newsapplication.ui.home.newsAdapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.shidqi.newsapplication.R
import com.shidqi.newsapplication.databinding.NewsLayoutItemBinding
import com.shidqi.newsapplication.models.Article
import java.text.SimpleDateFormat
import java.util.*


class NewsAdapter(private val context:Context, private val itemClick: (data: Article) -> Unit,) : PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(DiffUtilCallBack) {
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsLayoutItemBinding.inflate(
            LayoutInflater.from(context), parent,
            false
        )
        return NewsViewHolder(
            binding, context, itemClick
        )
    }
    object DiffUtilCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    inner class NewsViewHolder(
        private val binding: NewsLayoutItemBinding,
        private val context: Context,
        private val itemClick: (data: Article) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val formatter = SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        private var myOptions = RequestOptions()
            .centerCrop().override(250, 200)

        fun bind(data: Article) {

            binding.root.setOnClickListener {
                itemClick(data)
            }
            val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                System.currentTimeMillis()
            } else {
                Calendar.getInstance().timeInMillis

            }

            val newDate = formatter.parse(data.publishedAt).time
            val hours =  ((newDate - date) / 3600000) * -1


            binding.tvNewsTittle.text = data.title
            binding.tvTime.text = "$hours Hours Ago"

            Glide.with(context)

                .load(data.urlToImage)
                .apply(myOptions).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_error_24)
                .into(binding.ivNewsImage)

        }


    }
}
