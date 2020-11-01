package com.example.mytjournal.ui.feed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytjournal.R
import com.example.mytjournal.data.model.Post

class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var viewModel: FeedViewModel
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false

    val paginationListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!isLoading && !isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    startLoading()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.feed_fragment, container, false)
        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        progressBar = view.findViewById(R.id.pb_feed)
        recyclerView = view.findViewById(R.id.rv_feed)

        postsAdapter = PostsAdapter(mutableListOf())
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = postsAdapter
            addOnScrollListener(paginationListener)
        }

        viewModel.posts.observe(this as LifecycleOwner, Observer {
            showPosts(it)
            stopLoading()
        })
        startLoading()
        return view
    }

    fun startLoading() {
        isLoading = true
        progressBar.visibility = View.VISIBLE
        viewModel.loadPosts()
    }

    fun stopLoading() {
        isLoading = false
        progressBar.visibility = View.GONE
    }

    fun showPosts(posts: MutableList<Post>) {
        postsAdapter.posts.addAll(posts)
        postsAdapter.notifyDataSetChanged()
    }

}