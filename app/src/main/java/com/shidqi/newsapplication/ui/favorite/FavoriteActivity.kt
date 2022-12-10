package com.shidqi.newsapplication.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.shidqi.newsapplication.R
import com.shidqi.newsapplication.databinding.ActivityFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel : FavoriteViewModel by viewModels()
    private lateinit var favoriteNewsAdapter: FavoriteNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        bindViewModel()
        setupAdapter()
        setContentView(binding.root)
    }

    private fun bindViewModel(){
        viewModel.listOfNews.observe(this){
            if (it.isEmpty()){
                binding.rvFavorite.visibility = View.INVISIBLE
                binding.tvEmptyState.isVisible = true
            }else{
                binding.tvEmptyState.isVisible = false
                favoriteNewsAdapter.addData(it)
            }
        }
    }

    private fun setupAdapter(){
        favoriteNewsAdapter = FavoriteNewsAdapter(
            this,
            itemClick = {

            },
            removeFavorite = {article ->
                viewModel.deleteNews(article)
            }
        )
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvFavorite.layoutManager = layoutManager

        binding.rvFavorite.adapter = favoriteNewsAdapter
    }
}