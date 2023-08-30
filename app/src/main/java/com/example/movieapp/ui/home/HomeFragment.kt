package com.example.movieapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.ui.home.adapters.GenresAdapter
import com.example.movieapp.ui.home.adapters.LastMoviesAdapter
import com.example.movieapp.ui.home.adapters.TopMoviesAdapter
import com.example.movieapp.ui.movies.AllMovieFragment
import com.example.movieapp.utils.CheckConnection
import com.example.movieapp.utils.MyResponse
import com.example.movieapp.utils.initRecycler
import com.example.movieapp.utils.isVisible
import com.example.movieapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var topMoviesAdapter: TopMoviesAdapter

    @Inject
    lateinit var genresAdapter: GenresAdapter

    @Inject
    lateinit var lastMovesAdapter: LastMoviesAdapter

    @Inject
    lateinit var connection: CheckConnection

    enum class PageState {
        EMPTY,
        NETWORK,
        SERVER ,
        NONE
    }
    private var categoryId = 0
    private var selectedCategory = -1


    //Other
    private val viewModel: HomeViewModel by viewModels()
    private val pagerHelper: PagerSnapHelper by lazy { PagerSnapHelper() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Get top movies
            viewModel.topMoviesList.observe(viewLifecycleOwner) {

                topMoviesAdapter.differ.submitList(it.data?.data)
                //RecyclerView
                topMoviesRecycler.initRecycler(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    topMoviesAdapter
                )
                //Indicator
                pagerHelper.attachToRecyclerView(topMoviesRecycler)
                topMoviesIndicator.attachToRecyclerView(topMoviesRecycler, pagerHelper)
            }
            //get genres
            viewModel.genresList.observe(viewLifecycleOwner) {

                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        genresLoading.isVisible(true, genresRecycler)

                    }

                    MyResponse.Status.SUCCESS -> {
                        genresLoading.isVisible(false, genresRecycler)

                        genresAdapter.differ.submitList(it.data)
                        genresRecycler.initRecycler(
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            ),
                            genresAdapter

                        )
                    }

                    MyResponse.Status.ERROR -> {
                        genresLoading.isVisible(false, genresRecycler)

                        checkConnectionOrEmpty(true, PageState.SERVER)

                    }
                }

            }
            //Select genres
            genresAdapter.setOnItemClickListener { category, selecterd ->
                selectedCategory = selecterd
                categoryId = category.id

                if (selecterd < 0) {
                    viewModel.loadLastMoviesList()
                    lastMoviesTitle.text = "Last movies"
                } else {
                    lastMoviesTitle.text = "${category.name} movies"
                    viewModel.loadGenresMoviesList(category.id)

                }
            }
            //Get last movies
            viewModel.lastMoviesList.observe(viewLifecycleOwner) {
                when (it.status) {

                    MyResponse.Status.LOADING -> {
                        moviesLoading.isVisible(true, lastMoviesRecycler)

                    }

                    MyResponse.Status.SUCCESS -> {
                        moviesLoading.isVisible(false, lastMoviesRecycler)

                        lastMovesAdapter.setData(it.data?.data!!)
                        lastMoviesRecycler.initRecycler(
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            ),
                            lastMovesAdapter

                        )


                    }

                    MyResponse.Status.ERROR -> {
                        moviesLoading.isVisible(false, lastMoviesRecycler)
                        checkConnectionOrEmpty(true, PageState.SERVER)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
            //Click
            lastMovesAdapter.setOnItemClickListener {
                val direction = HomeFragmentDirections.actionToDetail(it.id!!.toInt())
                findNavController().navigate(direction)
            }
            //Internet
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
                homeDisLay.isVisible(true, moviesScrollLay)
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

                homeDisLay.isVisible(false, moviesScrollLay)
                viewModel.loadTopMoviesList(3)
                viewModel.loadGenresList()
                if (selectedCategory < 0) {
                    viewModel.loadLastMoviesList()

                } else {
                    viewModel.loadGenresMoviesList(categoryId)

                }
            }
        }


    }

}