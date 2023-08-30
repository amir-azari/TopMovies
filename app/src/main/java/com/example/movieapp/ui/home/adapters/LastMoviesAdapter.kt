package com.example.movieapp.ui.home.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.databinding.ItemHomeMoviesLastBinding
import com.example.movieapp.databinding.ItemHomeMoviesTopBinding
import com.example.movieapp.models.home.ResponseMoviesList.*
import javax.inject.Inject

class LastMoviesAdapter @Inject constructor() :
    RecyclerView.Adapter<LastMoviesAdapter.ViewHolder>() {

    private lateinit var binding: ItemHomeMoviesLastBinding
    private var moviesList = emptyList<Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastMoviesAdapter.ViewHolder {
        binding = ItemHomeMoviesLastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: LastMoviesAdapter.ViewHolder, position: Int) {
        holder.bindItems(moviesList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = moviesList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(item: Data) {
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

    fun setData(data: List<Data>) {
        val genresDiffUtils = GenresDiffUtils(moviesList , data)
        val diffUtils = DiffUtil.calculateDiff(genresDiffUtils)
        moviesList = data
        diffUtils.dispatchUpdatesTo(this)

    }

    class GenresDiffUtils(private val oldItem: List<Data>, private val newItem: List<Data>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size

        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

    }
}