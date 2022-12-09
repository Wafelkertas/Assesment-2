package com.shidqi.newsapplication.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shidqi.newsapplication.ui.search.searchNews.SearchNewsFragment
import com.shidqi.newsapplication.ui.search.searchSources.SearchSourcesFragment

/**
 * Adapter for ViewPager2
 * **/
class FragmentAdapter(
    private val listOfPages: List<Fragment>, val fragment: Fragment , val fm: FragmentManager
) : FragmentStateAdapter(fm, fragment.lifecycle) {

    override fun getItemCount(): Int = listOfPages.size

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return when (position) {
            0 -> listOfPages[0]
            else -> listOfPages[1]
        }
    }
}

