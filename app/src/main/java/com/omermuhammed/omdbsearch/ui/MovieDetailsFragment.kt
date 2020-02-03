package com.omermuhammed.omdbsearch.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.omermuhammed.omdbsearch.R
import com.omermuhammed.omdbsearch.di.Injectable
import com.omermuhammed.omdbsearch.network.Resource
import com.omermuhammed.omdbsearch.viewmodel.MovieDetailsViewModel
import javax.inject.Inject


class MovieDetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels {
        viewModelFactory
    }

    val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var fragmentContainer: ConstraintLayout

    private lateinit var poster: ImageView
    private lateinit var title: AppCompatTextView
    private lateinit var director: AppCompatTextView
    private lateinit var actors: AppCompatTextView
    private lateinit var plot: AppCompatTextView
    private lateinit var rating: AppCompatTextView
    private lateinit var imdbRating: AppCompatTextView

    private lateinit var progressBar: ProgressBar

    private lateinit var addRemoveFromFavorites: AppCompatButton

    private lateinit var movieTitle: String
    private lateinit var moviePosterUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_moviedetails, container, false)

        fragmentContainer = root.findViewById(R.id.fragment_moviedetails_layout)

        poster = root.findViewById(R.id.poster_image)

        title = root.findViewById(R.id.movie_title)

        director = root.findViewById(R.id.movie_director)

        actors = root.findViewById(R.id.movie_actors)

        plot = root.findViewById(R.id.movie_plot)

        rating = root.findViewById(R.id.movie_rating)

        imdbRating = root.findViewById(R.id.movie_imdbrating)

        progressBar = root.findViewById(R.id.movie_spinner)

        addRemoveFromFavorites = root.findViewById(R.id.add_remove_from_favorites)

        addRemoveFromFavorites.setOnClickListener {

            if (args.fromHomeScreen) {
                movieDetailsViewModel.saveToFavorites(args.movieDetailsId, movieTitle, moviePosterUrl)
                addRemoveFromFavorites.text = this.resources.getString(R.string.saved_to_favorites)
            } else {
                movieDetailsViewModel.removeFromFavorites(args.movieDetailsId)
                addRemoveFromFavorites.text = this.resources.getString(R.string.removed_from_favorites)
            }
        }

        getMovieDetailsFromServer()

        displayMovieDetails()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.fromHomeScreen) {
            addRemoveFromFavorites.text = this.resources.getString(R.string.add_favorites_button)
        } else {
            addRemoveFromFavorites.text = this.resources.getString(R.string.remove_favorites_button)
        }
    }


    private fun getMovieDetailsFromServer() {
        movieDetailsViewModel.fetchMovieDetails(args.movieDetailsId)
    }

    private fun displayMovieDetails() {
        movieDetailsViewModel.getMovieDetails().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    addRemoveFromFavorites.visibility = View.GONE
                }

                is Resource.Success -> {

                    progressBar.visibility = View.GONE
                    addRemoveFromFavorites.visibility = View.VISIBLE

                    poster.visibility = View.VISIBLE
                    title.visibility = View.VISIBLE
                    director.visibility = View.VISIBLE
                    actors.visibility = View.VISIBLE
                    plot.visibility = View.VISIBLE
                    rating.visibility = View.VISIBLE
                    imdbRating.visibility = View.VISIBLE


                    Glide.with(this)
                            .load(it.data.Poster)
                            .into(poster)

                    title.text = it.data.Title

                    movieTitle = it.data.Title
                    moviePosterUrl = it.data.Poster

                    director.text = this.resources.getString(
                            R.string.directed_by,
                            it.data.Director
                    )

                    actors.text = this.resources.getString(
                            R.string.starring,
                            it.data.Actors
                    )

                    plot.text = this.resources.getString(
                            R.string.plot,
                            it.data.Plot
                    )

                    rating.text = this.resources.getString(
                            R.string.rating,
                            it.data.Rated
                    )

                    imdbRating.text = this.resources.getString(
                            R.string.imdb_rating,
                            it.data.imdbRating
                    )
                }

                is Resource.Failure -> {

                    poster.visibility = View.GONE
                    addRemoveFromFavorites.visibility = View.GONE

                    progressBar.visibility = View.GONE

                    title.visibility = View.GONE

                    director.visibility = View.VISIBLE
                    director.text = it.errorMessage

                    actors.visibility = View.GONE
                    plot.visibility = View.GONE
                    rating.visibility = View.GONE
                    imdbRating.visibility = View.GONE
                }
            }

        })
    }
}