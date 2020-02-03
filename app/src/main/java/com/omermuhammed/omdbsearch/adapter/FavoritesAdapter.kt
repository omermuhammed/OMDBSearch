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
import com.omermuhammed.omdbsearch.data.FavoritesEntity
import com.omermuhammed.omdbsearch.ui.FavoritesFragmentDirections

class FavoritesAdapter internal constructor(context: FragmentActivity?) :
    ListAdapter<FavoritesEntity, FavoritesAdapter.FavoritesViewHolder>(
            FavoritesDiffCallback()
    ) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var favoritesList: List<FavoritesEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemView = inflater.inflate(R.layout.favorites_view_item, parent, false)
        return FavoritesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        if (favoritesList.isEmpty()) {
            holder.poster.setImageResource(R.drawable.baseline_insert_photo_24)
            holder.title.setText(R.string.empty_photo)
        } else {
            val favoritesEntity: FavoritesEntity = favoritesList[position]

            holder.title.text = favoritesEntity.Title

            val url: String? = favoritesEntity.Poster

            // sometimes the server endpoint doesn't have multimedia data..
            if (url.isNullOrEmpty()) {
                holder.poster.setImageResource(R.drawable.baseline_insert_photo_24)
            } else {
                Glide.with(holder.itemView.context)
                    .load(url)
                    .centerInside()
                    .into(holder.poster)
            }

            val action: NavDirections = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(favoritesEntity.imdbID, fromHomeScreen = false)
            holder.itemView.setOnClickListener {
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount() = favoritesList.size

    internal fun setFavoritesInAdapter(list: List<FavoritesEntity>) {
        this.favoritesList = list
        notifyDataSetChanged()
    }

    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var poster: ImageView = itemView.findViewById(R.id.favorites_viewitem_poster)
        var title: TextView = itemView.findViewById(R.id.favorites_viewitem_title)
    }
}


class FavoritesDiffCallback : DiffUtil.ItemCallback<FavoritesEntity>() {

    override fun areItemsTheSame(
            oldItem: FavoritesEntity,
            newItem: FavoritesEntity
    ): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(
            oldItem: FavoritesEntity,
            newItem: FavoritesEntity
    ): Boolean {
        return oldItem == newItem
    }
}