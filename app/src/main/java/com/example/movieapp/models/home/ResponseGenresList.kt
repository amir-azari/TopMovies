package com.example.movieapp.models.home

import com.google.gson.annotations.SerializedName

class ResponseGenresList : ArrayList<ResponseGenresList.ResponseGenresListItem>(){
    data class ResponseGenresListItem(
        var isSelected: Boolean = false,

        @SerializedName("id")
        val id: Int = 0, // 21
        @SerializedName("name")
        val name: String = "" // Sport
    )
}