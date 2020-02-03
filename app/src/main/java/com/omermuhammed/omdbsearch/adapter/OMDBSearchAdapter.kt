package com.omermuhammed.omdbsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omermuhammed.omdbsearch.R
import com.omermuhammed.omdbsearch.data.SearchEntity
import com.omermuhammed.omdbsearch.ui.HomeFragmentDirections

class OMDBSearchAdapter internal constructor(context: FragmentActivity?) :
    ListAdapter<SearchEntity, OMDBSearchAdapter.OMDBSearchViewHolder>(
        OMDBSearchDiffCallback()
    ) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var searchResultsList: List<SearchEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OMDBSearchViewHolder {
        val itemView = inflater.inflate(R.layout.omdbsearch_view_item, parent, false)
        return OMDBSearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OMDBSearchViewHolder, position: Int) {
        if (searchResultsList.isEmpty()) {
            holder.posterImage.setImageResource(R.drawable.baseline_insert_photo_24)
            holder.title.setText(R.string.empty_photo)
        } else {
            val searchEntity: SearchEntity = searchResultsList[position]

            holder.title.text = searchEntity.Title

            holder.year.text = holder.itemView.context.getString(
                    R.string.year,
                    searchEntity.Year
            )

            holder.type.text = holder.itemView.context.getString(
                    R.string.type_of_media,
                    searchEntity.Type
            )

            val url: String? = searchEntity.Poster

            // sometimes the server endpoint doesn't have multimedia data..
            if (url.isNullOrEmpty()) {
                holder.posterImage.setImageResource(R.drawable.baseline_insert_photo_24)
            } else {
                Glide.with(holder.itemView.context)
                    .load(url)
                    .centerInside()
                    .into(holder.posterImage)
            }

            val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(searchEntity.imdbID, true)
            holder.itemView.setOnClickListener {
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount() = searchResultsList.size

    internal fun setResultsInAdapter(results: List<SearchEntity>) {
        this.searchResultsList = results
        notifyDataSetChanged()
    }

    class OMDBSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var posterImage: ImageView = itemView.findViewById(R.id.favorites_poster)
        var title: TextView = itemView.findViewById(R.id.favorites_title)
        var year: TextView = itemView.findViewById(R.id.year_of_release)
        var type: TextView = itemView.findViewById(R.id.type_of_content)
    }
}

class OMDBSearchDiffCallback : DiffUtil.ItemCallback<SearchEntity>() {

    override fun areItemsTheSame(
        oldItem: SearchEntity,
        newItem: SearchEntity
    ): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(
        oldItem: SearchEntity,
        newItem: SearchEntity
    ): Boolean {
        return oldItem == newItem
    }
}