package com.shidqi.newsapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.shidqi.newsapplication.databinding.ActivityMainBinding
import com.shidqi.newsapplication.databinding.WebviewBinding
import com.shidqi.newsapplication.ui.favorite.FavoriteActivity
import com.shidqi.newsapplication.ui.home.newsAdapter.NewsAdapter
import com.shidqi.newsapplication.utils.Resource
import com.shidqi.newsapplication.ui.home.newsAdapter.NewsLoadStateAdapter
import com.shidqi.newsapplication.ui.search.SearchActivity
import com.shidqi.newsapplication.ui.home.sourceAdapter.SourceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class HomeActivty : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var sourceAdapter: SourceAdapter
    private lateinit var newsAdapter: NewsAdapter
    private var backPressedCounter = 1

    private val categoryList = mutableListOf(
        "business",
        "entertainment",
        "general",
        "health",
        "science",
        "sports",
        "technology"
    )


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initView()
        bindViewModels()
        setupAdapter()

    }

    /**
     * Intantiate the view such as View.onClickListener, inflating view, set TextView and set an ImageView
     * **/
    private fun initView() {
        binding.etSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.chipGroup.setOnCheckedStateChangeListener { chip, checked ->
            backPressedCounter = 1
            if (checked.isEmpty()) {
                binding.chipGroup.check(homeViewModel.categoryPosition)
            }
            if (checked.isNotEmpty()) {
                homeViewModel.category = categoryList[checked.first()]
                homeViewModel.categoryPosition = checked.first()
            }
            homeViewModel.getSources()
            homeViewModel.showingNews.value = false
        }
        binding.chipGroup.check(binding.chipGroup.getChildAt(0).id)

        binding.ivFavorite.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
    }

    /**
     * Binding livedata objects on viewModel and observing it
     * **/
    private fun bindViewModels() {
        lifecycleScope.launch {
            homeViewModel.newsListWithPaging.observe(this@HomeActivty) {
                newsAdapter.submitData(this@HomeActivty.lifecycle, it)
            }

        }

        homeViewModel.listOfSource.observe(this) {
            when (it) {
                is Resource.Success -> {
                    if (it.data != null) {

                        sourceAdapter.update(it.data)


                    }
                    binding.tvSource.isVisible = true
                    binding.progressCircular.visibility = View.GONE
                    binding.sourceContainer.visibility = View.VISIBLE
                    hideError()

                }
                is Resource.Error -> {

                    showError(it.message!!)
                    binding.progressCircular.visibility = View.GONE
                    binding.sourceContainer.visibility = View.VISIBLE
                    binding.newsContainer.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    hideError()
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.sourceContainer.visibility = View.GONE
                    binding.newsContainer.visibility = View.GONE
                }
                else -> {}
            }
        }


    }

    override fun onBackPressed() {
        binding.sourceContainer.isVisible = true
        binding.newsContainer.isVisible = false

        if (backPressedCounter == 2){
            super.onBackPressed()
        }
        backPressedCounter++
    }

    /**
     * Create an adapter for all the recyclerview
     * **/
    private fun setupAdapter() {
        newsAdapter = NewsAdapter(this) {article->
            val webViewBinding = WebviewBinding.inflate(this@HomeActivty.layoutInflater)
            setContentView(webViewBinding.root)
            webViewBinding.webView.loadUrl(article.url)
            webViewBinding.backButton.setOnClickListener {
                setContentView(binding.root)
            }
            val alreadyFavorite = runBlocking{
                homeViewModel.findNewsInDatabase(article)
            }
            if (alreadyFavorite){
                webViewBinding.ivFavorite.visibility = View.INVISIBLE
                webViewBinding.ivFavorited.visibility = View.VISIBLE
                webViewBinding.ivFavorited.setOnClickListener {
                    homeViewModel.deleteNews(article)
                }
            }else{
                webViewBinding.ivFavorited.visibility = View.INVISIBLE
                webViewBinding.ivFavorite.visibility = View.VISIBLE
                webViewBinding.ivFavorite.setOnClickListener {
                    homeViewModel.insertNewsToDatabase(article)
                }
            }

        }


        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvNews.layoutManager = layoutManager


        binding.rvNews.adapter = newsAdapter
        binding.rvNews.setHasFixedSize(true)
        binding.rvNews.itemAnimator = null
        binding.rvNews.adapter = newsAdapter.withLoadStateHeaderAndFooter(
            header = NewsLoadStateAdapter { newsAdapter.retry() },
            footer = NewsLoadStateAdapter { newsAdapter.retry() }
        )

        newsAdapter.addLoadStateListener { loadState ->

            binding.progressCircular.isVisible = loadState.source.refresh is LoadState.Loading
            if (loadState.source.refresh is LoadState.Error){
                showError("Error Something Happened")
            }else{
                hideError()
            }

            if (loadState.source.refresh !is LoadState.Error && loadState.source.refresh !is LoadState.Loading && newsAdapter.itemCount == 0){
                showError("No News Found ")
            }



        }

        sourceAdapter = SourceAdapter(context = this, onClick = {
            newsAdapter.submitData(lifecycle, PagingData.empty())
            binding.tvNews.text = "News from ${it.name}"
            binding.tvNews.visibility = View.VISIBLE
            homeViewModel.currentQuery.value = it.id
            binding.sourceContainer.isVisible = false
            binding.newsContainer.isVisible = true
            backPressedCounter = 1
        })

        binding.rvSources.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSources.adapter = sourceAdapter
    }

    /**
     * Function to show error container
     * **/
    private fun showError(errorMessage: String) {
        binding.errorContainer.visibility = View.VISIBLE
        binding.tvNews.isVisible = false
        binding.tvSource.isVisible = false
        binding.tvError.text = errorMessage
        binding.rvNews.visibility = View.GONE
        binding.rvSources.visibility = View.GONE
    }

    /**
     * Function to hide error container
     * **/
    private fun hideError() {
        binding.errorContainer.visibility = View.GONE
        binding.rvNews.visibility = View.VISIBLE
        binding.rvSources.visibility = View.VISIBLE

    }


}

