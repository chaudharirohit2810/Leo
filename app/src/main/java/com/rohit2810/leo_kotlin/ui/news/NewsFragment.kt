package com.rohit2810.leo_kotlin.ui.news

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.database.getDatabase
import com.rohit2810.leo_kotlin.models.news.NewsDatabaseModule
import com.rohit2810.leo_kotlin.utils.getLatitudeFromCache
import com.rohit2810.leo_kotlin.utils.getLongitudeFromCache
import kotlinx.android.synthetic.main.fragment_news.*
import timber.log.Timber
import java.util.*


class NewsFragment : Fragment() {

    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsViewModelFactory: NewsViewModelFactory
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "News"
        var view = inflater.inflate(R.layout.fragment_news, container, false)
        newsViewModelFactory = NewsViewModelFactory(requireContext())
        newsViewModel = ViewModelProvider(this, newsViewModelFactory).get(NewsViewModel::class.java)
        val gcd = Geocoder(requireActivity(), Locale.getDefault())
        val addresses: List<Address> =
            gcd.getFromLocation(
                getLatitudeFromCache(requireContext()).toDouble(),
                getLongitudeFromCache(requireContext()).toDouble(),
                1
            )
        if (addresses.size > 0) {
            newsViewModel.city = addresses[0].getLocality()
            newsViewModel.insertNews()
        }
        adapter = NewsAdapter(requireContext()) { news ->
            newsArticleClicked(news)
        }
        recyclerView = view.findViewById(R.id.news_rv)
        val dividerItemDecoration =
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
        getDatabase(requireContext()).newsDao.getAllNews().observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    progressBar3.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                    adapter.setArray(it)
                }
            }
        })
        return view
    }

    fun newsArticleClicked(news: NewsDatabaseModule) {
        startActivity (Intent(Intent.ACTION_VIEW, Uri.parse(news.url)))
    }

}