package com.omermuhammed.omdbsearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omermuhammed.omdbsearch.R
import com.omermuhammed.omdbsearch.adapter.FavoritesAdapter
import com.omermuhammed.omdbsearch.di.Injectable
import com.omermuhammed.omdbsearch.network.Resource
import com.omermuhammed.omdbsearch.utils.autoCleared
import com.omermuhammed.omdbsearch.viewmodel.FavoritesViewModel
import javax.inject.Inject

class FavoritesFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel: FavoritesViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var favoritesRecyclerView: RecyclerView

    private var favoritesAdapter: FavoritesAdapter by this.autoCleared()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var favoritesFragmentContainer: ConstraintLayout

    private lateinit var favoritesPlaceholderText: AppCompatTextView

    private lateinit var favoritesProgressBar: ProgressBar

//    private lateinit var scrollListener: RecyclerView.OnScrollListener

//    private val lastVisibleItemPosition: Int get() = linearLayoutManager.findLastVisibleItemPosition()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.fragment_favorites, container, false)

        favoritesRecyclerView = root.findViewById(R.id.favorites_recyclerview)

        favoritesFragmentContainer = root.findViewById(R.id.fragment_favorites_layout)

        linearLayoutManager = LinearLayoutManager(activity)

        favoritesRecyclerView.layoutManager = linearLayoutManager

        favoritesAdapter = FavoritesAdapter(activity)

        favoritesRecyclerView.adapter = favoritesAdapter

//        setRecyclerViewScrollListener()

        favoritesPlaceholderText = root.findViewById(R.id.favorites_placeholder_text)

        favoritesProgressBar = root.findViewById(R.id.favorites_spinner)

        loadFavoritesFromDB()

        return root
    }

    private fun getSearchResults() {
        // use viewLifecycleOwner, instead of 'this'
        // In a fragment the view can be created more than once so you end up
        // making .observe call more than once, if the lifecycleOwner is the
        // fragment itself. But if you use viewLifecycleOwner the view is only
        // recreated after its been destroyed so the observer is
        // automatically cleaned up.
        homeViewModel.getFavoritesList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    favoritesProgressBar.visibility = View.VISIBLE
                    favoritesPlaceholderText.visibility = View.GONE
                    favoritesRecyclerView.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    favoritesProgressBar.visibility = View.GONE
                    favoritesPlaceholderText.visibility = View.GONE

//                    if (pageNumber > totalNumOfPages) {
//                        displayNoMoreResultsMessage()
//                    } else {
//                        favoritesAdapter.setFavoritesInAdapter(it.data)
//                    }
                    favoritesAdapter.setFavoritesInAdapter(it.data)

                    favoritesRecyclerView.visibility = View.VISIBLE
                }

                is Resource.Failure -> {
                    favoritesProgressBar.visibility = View.GONE
                    favoritesRecyclerView.visibility = View.GONE
                    favoritesPlaceholderText.text = it.errorMessage
                    favoritesPlaceholderText.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun loadFavoritesFromDB() {
        homeViewModel.fetchFavorites()

        favoritesPlaceholderText.visibility = View.GONE
        favoritesRecyclerView.visibility = View.VISIBLE

        getSearchResults()
    }

    // Leaving this in so that we can do endless scrolling, if needed
//    private fun setRecyclerViewScrollListener() {
//        scrollListener = object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(rv, newState)
//
//                val totalItemCount: Int? = rv.layoutManager?.itemCount
//
//                if (totalItemCount == lastVisibleItemPosition + 1) {
//                    if (totalNumOfPages >= pageNumber) {
//                        getDataFromViewModel()
//                    } else {
//                        displayNoFavoritesMessage()
//                    }
//                }
//            }
//        }
//        favoritesRecyclerView.addOnScrollListener(scrollListener)
//    }

//    private fun displayNoMoreResultsMessage() {
//        val noMoreResultsSnackbar: Snackbar = Snackbar.make(
//                favoritesFragmentContainer,
//                this.getString(
//                        R.string.no_more_favorites
//                ),
//                Snackbar.LENGTH_LONG
//        )
//
//        noMoreResultsSnackbar.show()
//    }
//
//    private fun displayNoFavoritesMessage() {
//        favoritesProgressBar.visibility = View.GONE
//        favoritesRecyclerView.visibility = View.GONE
//        favoritesPlaceholderText.text = this.resources.getString(R.string.no_favorites_in_db_message)
//        favoritesPlaceholderText.visibility = View.VISIBLE
//    }
}