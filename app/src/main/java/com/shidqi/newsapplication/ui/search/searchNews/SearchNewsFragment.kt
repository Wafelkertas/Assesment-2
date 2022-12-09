package com.shidqi.newsapplication.ui.search.searchNews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shidqi.newsapplication.databinding.FragmentSearchNewsBinding
import com.shidqi.newsapplication.databinding.WebviewBinding
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.ui.home.newsAdapter.NewsAdapter
import com.shidqi.newsapplication.ui.home.newsAdapter.NewsLoadStateAdapter
import com.shidqi.newsapplication.utils.Resource
import com.shidqi.newsapplication.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment(
    val onTap: (data: Article) -> Unit
) : Fragment() {
    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)

        setupAdapter()
        bindViewModels()



        return binding.root
    }


    /**
     * Binding livedata objects on viewModel and observing it
     * **/
    private fun bindViewModels() {
        lifecycleScope.launch {
            viewModel.newsListWithPaging.observe(viewLifecycleOwner){
                newsAdapter.submitData(lifecycle, it)
            }

        }

    }

    private fun setupAdapter() {
        newsAdapter = NewsAdapter(requireContext()){
            val webViewBinding = WebviewBinding.inflate(this.layoutInflater)
            binding.root.apply {
                addView(webViewBinding.root)
                webViewBinding.webView.loadUrl(it.url)
                webViewBinding.backButton.setOnClickListener {
                    removeView(webViewBinding.root)
                }
            }


        }
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvSearchNews.layoutManager = layoutManager

        binding.rvSearchNews.adapter = newsAdapter
        binding.rvSearchNews.setHasFixedSize(true)
        binding.rvSearchNews.itemAnimator = null
        binding.rvSearchNews.adapter = newsAdapter.withLoadStateHeaderAndFooter(
            header = NewsLoadStateAdapter { newsAdapter.retry() },
            footer = NewsLoadStateAdapter { newsAdapter.retry() }
        )

        newsAdapter.addLoadStateListener { loadState ->

            binding.progressCircular.isVisible = loadState.source.refresh is LoadState.Loading
            binding.rvSearchNews.isVisible = loadState.source.refresh is LoadState.NotLoading


            // empty view
            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                newsAdapter.itemCount < 1
            ) {
                binding.rvSearchNews.isVisible = false
//                    textViewEmpty.isVisible = true
            } else {
//                    textViewEmpty.isVisible = false
            }

        }
    }

    private fun showError(){
        binding.errorContainer.visibility = View.VISIBLE
        binding.rvSearchNews.visibility = View.GONE
    }
    private fun hideError(){
        binding.errorContainer.visibility = View.GONE
        binding.rvSearchNews.visibility = View.VISIBLE
    }

}