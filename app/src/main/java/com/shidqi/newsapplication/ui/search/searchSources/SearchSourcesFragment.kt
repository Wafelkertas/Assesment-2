package com.shidqi.newsapplication.ui.search.searchSources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shidqi.newsapplication.databinding.FragmentSearchSourcesBinding
import com.shidqi.newsapplication.utils.Resource
import com.shidqi.newsapplication.ui.search.SearchViewModel
import com.shidqi.newsapplication.ui.home.sourceAdapter.SourceAdapter


class SearchSourcesFragment(
    private val callback : () -> Unit
): Fragment() {

    private lateinit var binding: FragmentSearchSourcesBinding
    private lateinit var adapter: SourceAdapter
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchSourcesBinding.inflate(inflater, container, false)
        bindViewModels()
        setupAdapter()
        return binding.root

    }

    /**
     * Binding livedata objects on viewModel and observing it
     * **/
    private fun bindViewModels() {
        viewModel.listOfSources.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progressCircular.visibility = View.GONE
                    showError()
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        adapter.update(it.data)


                    }
                    hideError()
                    binding.progressCircular.visibility = View.GONE
                    binding.rvSearchSource.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    hideError()
                    binding.progressCircular.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = SourceAdapter(requireContext(), onClick = {
            viewModel.currentQuery.value = it.name
            callback()
        })
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvSearchSource.layoutManager = layoutManager

        binding.rvSearchSource.adapter = adapter


    }
    private fun showError(){
        binding.errorContainer.visibility = View.VISIBLE
        binding.rvSearchSource.visibility = View.GONE
    }
    private fun hideError(){
        binding.errorContainer.visibility = View.GONE
        binding.rvSearchSource.visibility = View.GONE
    }
}