package com.shidqi.newsapplication.ui.favorite

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.shidqi.newsapplication.R
import com.shidqi.newsapplication.databinding.NewsLayoutItemFavoriteBinding
import com.shidqi.newsapplication.models.Article
import java.text.SimpleDateFormat
import java.util.*

class FavoriteNewsAdapter(
    private val context: Context,
    var itemsList: MutableList<Article> = mutableListOf(),
    private val itemClick: (data: Article) -> Unit,
    private val removeFavorite: (data: Article) -> Unit,
) : RecyclerView.Adapter<FavoriteNewsAdapter.FavoriteNewsViewHolder>() {


    fun addData(items: List<Article>) {
        itemsList.clear()
        itemsList.addAll(items)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteNewsAdapter.FavoriteNewsViewHolder {
        return FavoriteNewsViewHolder(
            NewsLayoutItemFavoriteBinding.inflate(
                LayoutInflater.from(context), parent,
                false
            ), context, itemClick,removeFavorite
        )
    }

    override fun onBindViewHolder(holder: FavoriteNewsViewHolder, position: Int) {

        holder.bind(this.itemsList[position])
    }

    override fun getItemCount(): Int {
        return this.itemsList.size
    }

    fun getItemSize(): Int {
        return this.itemsList.size
    }

    inner class FavoriteNewsViewHolder(
        private val binding: NewsLayoutItemFavoriteBinding,
        private val context: Context,
        private val itemClick: (data: Article) -> Unit,
        private val removeFavorite: (data: Article) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val formatter = SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        private var myOptions = RequestOptions()
            .centerCrop().override(250, 200)

        fun bind(data: Article) {

            binding.ivRemove.setOnClickListener {
                removeFavorite(data)
            }
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