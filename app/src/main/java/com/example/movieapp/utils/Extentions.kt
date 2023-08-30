package com.example.movieapp.utils

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.ui.home.HomeFragment


fun View.isVisible(isShowLoading : Boolean , container : View) {

    if (isShowLoading){
        this.visibility = View.VISIBLE
        container.visibility = View.GONE
    }else{
        this.visibility = View.GONE
        container.visibility = View.VISIBLE

    }

}
fun View.showInvisible(isShown: Boolean) {
    if (isShown) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun RecyclerView.initRecycler(layoutManager: RecyclerView.LayoutManager, adapter: RecyclerView.Adapter<*>) {
    this.layoutManager = layoutManager
    this.adapter = adapter
}


