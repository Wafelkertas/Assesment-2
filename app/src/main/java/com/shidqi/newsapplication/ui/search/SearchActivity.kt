package com.shidqi.newsapplication.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.shidqi.newsapplication.databinding.ActivitySearchBinding
import com.shidqi.newsapplication.databinding.WebviewBinding
import com.shidqi.newsapplication.utils.Resource
import com.shidqi.newsapplication.ui.search.searchNews.SearchNewsFragment
import com.shidqi.newsapplication.ui.search.searchSources.SearchSourcesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val callback : () -> Unit = {

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        initView()


    }

    /**
     * Intantiate the view such as View.onClickListener, inflating view, set TextView and set an ImageView
     * **/
    private fun initView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {

                if (p0 != null) {
                    if (binding.pager.currentItem == 0) {
                        searchViewModel.currentQuery.value = p0
                    }

                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0.equals("")) {
                    this.onQueryTextSubmit("");
                }

                if (p0 != null) {
                    if (binding.pager.currentItem == 1) {
                        searchViewModel.searchQuery = p0
                        searchViewModel.filter(p0)
                    }
                }
                return true
            }

        })
    }

    private fun setupAdapter() {

        val adapter = FragmentAdapter(
            listOfPages = listOf(
                SearchNewsFragment(onTap = {
                    val webViewBinding = WebviewBinding.inflate(layoutInflater)
                    setContentView(webViewBinding.root)
                    webViewBinding.webView.loadUrl(it.url)
                    webViewBinding.backButton.setOnClickListener {
                        setContentView(binding.root)
                    }

                }),
                SearchSourcesFragment(callback = {
                    binding.pager.currentItem = 0
                })
            ), fm = supportFragmentManager, fragment = SearchNewsFragment(onTap = {
                val webViewBinding = WebviewBinding.inflate(layoutInflater)
                setContentView(webViewBinding.root)
                webViewBinding.webView.loadUrl(it.url)
                webViewBinding.backButton.setOnClickListener {
                    setContentView(binding.root)
                }

            })
        )
        binding.pager.adapter = adapter

        val tabLayout = binding.tabLayout


        TabLayoutMediator(tabLayout, binding.pager) { tab, position ->
            searchViewModel.selectedTab = position
            when (position) {
                0 -> {
                    tab.text = "Search News"
                }
                1 -> {
                    tab.text = "List Sources"
                }
            }
        }.attach()
    }
}