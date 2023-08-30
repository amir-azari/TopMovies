package com.example.movieapp.ui.movies


import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentAllMovieBinding
import com.example.movieapp.ui.home.HomeFragment
import com.example.movieapp.ui.home.HomeFragmentDirections
import com.example.movieapp.ui.search.SearchFragment
import com.example.movieapp.utils.CheckConnection
import com.example.movieapp.utils.MyResponse
import com.example.movieapp.utils.isVisible
import com.example.movieapp.viewmodel.AllMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AllMovieFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentAllMovieBinding

    //ViewModel
    private val viewModel: AllMoviesViewModel by viewModels()

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    @Inject
    lateinit var connection: CheckConnection
    enum class PageState {
        EMPTY,
        NETWORK,
        SERVER ,
        NONE
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.moviesList
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //InitView
        binding.apply {

            //Load data
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.moviesList.collect {
                        moviesAdapter.submitData(it)
                    }
                }
            }

            //Loading
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    moviesAdapter.loadStateFlow.collect {
                        val state = it.refresh
                        moviesLoading.isVisible = state is LoadState.Loading
                    }
                }
            }
            //Click
            moviesAdapter.setOnItemClickListener {
                val direction = HomeFragmentDirections.actionToDetail(it.id!!.toInt())
                findNavController().navigate(direction)
            }

            //RecyclerView
            moviesRecycler.apply {
                layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = moviesAdapter
            }
            connection.observe(viewLifecycleOwner) {
                if (it) {
                    checkConnectionOrEmpty(false, PageState.NONE)
                } else {
                    checkConnectionOrEmpty(true, PageState.NETWORK)
                }
            }

        }
    }
    private fun checkConnectionOrEmpty(isShownError: Boolean, state: PageState) {
        binding.apply {
            if (isShownError) {
                homeDisLay.isVisible(true, moviesLay)
                when (state) {
                    PageState.EMPTY -> {
                        statusLay.emptyImg.setImageResource(R.drawable.empty)
                        statusLay.emptyTxt.text = getString(R.string.emptyList)


                    }

                    PageState.NETWORK -> {
                        statusLay.emptyImg.setImageResource(R.drawable.ic_round_wifi_off_24)
                        statusLay.emptyTxt.text = getString(R.string.checkInternet)

                    }

                    PageState.SERVER -> {
                        statusLay.emptyImg.setImageResource(R.drawable.baseline_cloud_off_24)
                        statusLay.emptyTxt.text = getString(R.string.server)

                    }

                    else -> {

                    }
                }
            } else {
                homeDisLay.isVisible(false, moviesLay)
                moviesAdapter.refresh()

            }
        }

    }

}
