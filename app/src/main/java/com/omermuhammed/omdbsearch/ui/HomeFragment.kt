package com.omermuhammed.omdbsearch.ui

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.omermuhammed.omdbsearch.R
import com.omermuhammed.omdbsearch.adapter.OMDBSearchAdapter
import com.omermuhammed.omdbsearch.di.Injectable
import com.omermuhammed.omdbsearch.network.Resource
import com.omermuhammed.omdbsearch.utils.*
import com.omermuhammed.omdbsearch.viewmodel.HomeViewModel
import javax.inject.Inject

// Main fragment for the test app, note the the viewmodels are created through
// ViewModelFactory
class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel: HomeViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var recyclerView: RecyclerView

    private var OMDBSearchAdapter: OMDBSearchAdapter by this.autoCleared()

    private lateinit var scrollListener: RecyclerView.OnScrollListener

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fragmentContainer: ConstraintLayout

    private lateinit var placeholderText: AppCompatTextView

    private lateinit var progressBar: ProgressBar

    private val lastVisibleItemPosition: Int get() = linearLayoutManager.findLastVisibleItemPosition()

    private var pageNumber: Int = INITIAL_PAGE

    private var searchTerm: String = DEFAULT_SEARCH_TERM

    private var totalNumOfPages: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = root.findViewById(R.id.omdbsearch_recyclerview)

        fragmentContainer = root.findViewById(R.id.fragment_home_layout)

        linearLayoutManager = LinearLayoutManager(activity)

        recyclerView.layoutManager = linearLayoutManager

        OMDBSearchAdapter = OMDBSearchAdapter(activity)

        recyclerView.adapter = OMDBSearchAdapter

        setRecyclerViewScrollListener()

        placeholderText = root.findViewById(R.id.placeholder_text)

        progressBar = root.findViewById(R.id.home_spinner)

        setHasOptionsMenu(true)

        return root
    }

    override fun onResume() {
        super.onResume()

        if (searchTerm != DEFAULT_SEARCH_TERM) {
            startSearch()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.search_menu, menu)


        val searchView: SearchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        searchView.isIconified = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(str: String): Boolean {

                if (str.isNotEmpty()) {
                    searchTerm = str
                }

                searchView.clearFocus()
                searchView.isIconifiedByDefault = true

                startSearch()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    // simple listener for endless scrolling of search results
    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rv, newState)

                val totalItemCount: Int? = rv.layoutManager?.itemCount

                if (totalItemCount == lastVisibleItemPosition + 1) {
                    if (totalNumOfPages >= pageNumber) {
                        fetchResultsFromSearch()
                    } else {
                        displayNoMoreResultsMessage()
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    private fun displayNoMoreResultsMessage() {
        val noMoreResultsSnackbar: Snackbar = Snackbar.make(
            fragmentContainer,
            this.getString(
                R.string.no_more_results
            ),
            Snackbar.LENGTH_LONG
        )

        noMoreResultsSnackbar.show()
    }

    private fun getSearchResults() {
        // use viewLifecycleOwner, instead of 'this'
        // In a fragment the view can be created more than once so you end up
        // making .observe call more than once, if the lifecycleOwner is the
        // fragment itself. But if you use viewLifecycleOwner the view is only
        // recreated after its been destroyed so the observer is
        // automatically cleaned up.
        homeViewModel.getTotalNumOfPages() .observe(viewLifecycleOwner, Observer {
            totalNumOfPages = it
        })

        homeViewModel.getSearchResults().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    placeholderText.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    placeholderText.visibility = View.GONE

                    if (pageNumber > totalNumOfPages) {
                        displayNoMoreResultsMessage()
                    } else {
                        OMDBSearchAdapter.setResultsInAdapter(it.data)
                    }

                    recyclerView.visibility = View.VISIBLE
                }

                is Resource.Failure -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    placeholderText.text = it.errorMessage
                    placeholderText.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun startSearch() {
        pageNumber = INITIAL_PAGE
        totalNumOfPages = 0

        fetchResultsFromSearch()

        placeholderText.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        getSearchResults()
    }

    private fun fetchResultsFromSearch() {
        homeViewModel.fetchSearchResults(pageNumber, searchTerm)
        pageNumber += PAGE_INCREMENT
    }
}