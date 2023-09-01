package com.example.movieapp.ui.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.ItemHomeGenreListBinding
import com.example.movieapp.models.home.ResponseGenresList.ResponseGenresListItem
import com.example.movieapp.models.home.ResponseMoviesList.*
import javax.inject.Inject

class GenresAdapter @Inject constructor() : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    private lateinit var binding: ItemHomeGenreListBinding
    private var selectedItem = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresAdapter.ViewHolder {
        binding = ItemHomeGenreListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()

    }

    override fun onBindViewHolder(holder: GenresAdapter.ViewHolder, position: Int) {

        holder.setData(differ.currentList[position], position)
        holder.setIsRecyclable(false)

    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        fun setData(item: ResponseGenresListItem, position: Int) {
            binding.apply {
                nameTxt.text = item.name
                // Click handling
                root.setOnClickListener {
                    if (selectedItem == adapterPosition) {
                        selectedItem = -1
                    } else {
                        selectedItem = adapterPosition
                    }
                    notifyDataSetChanged()
                    onItemClickListener?.let {
                        it(item , selectedItem)
                    }
                }
                if (selectedItem == adapterPosition) {
                    root.setBackgroundResource(R.drawable.bg_rounded_selcted)
                    nameTxt.setTextColor(ContextCompat.getColor(nameTxt.context, R.color.chineseBlack))

                } else {
                    root.setBackgroundResource(R.drawable.bg_rounded_dark)

                }
            }
        }
    }

    private var onItemClickListener: ((ResponseGenresListItem , Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (ResponseGenresListItem, Int) -> Unit) {
        onItemClickListener = listener
    }


    private val differCallback = object : DiffUtil.ItemCallback<ResponseGenresListItem>() {
        override fun areItemsTheSame(
            oldItem: ResponseGenresListItem,
            newItem: ResponseGenresListItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseGenresListItem,
            newItem: ResponseGenresListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}