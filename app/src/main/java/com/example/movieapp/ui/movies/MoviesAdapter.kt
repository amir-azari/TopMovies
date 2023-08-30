package com.example.movieapp.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.databinding.ItemAllMoviesBinding
import com.example.movieapp.databinding.ItemHomeMoviesLastBinding
import com.example.movieapp.models.home.ResponseMoviesList.*
import javax.inject.Inject

class MoviesAdapter @Inject constructor() : PagingDataAdapter<Data, MoviesAdapter.ViewHolder>(differCallback) {

    private lateinit var binding: ItemAllMoviesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        binding = ItemAllMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        holder.setData(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        fun setData(item: Data) {
            binding.apply {
                movieNameTxt.text =item.title
                movieRateTxt.text = item.imdbRating
                movieCountryTxt.text =item.country
                movieYearTxt.text = item.year
                moviePosterImg.load(item.poster){
                    crossfade(true)
                    crossfade(800)
                }
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }
    private var onItemClickListener: ((Data) -> Unit)? = null

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }
        }
    }

}