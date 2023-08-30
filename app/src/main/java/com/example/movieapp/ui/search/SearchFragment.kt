package com.example.movieapp.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.ui.home.HomeFragment
import com.example.movieapp.ui.home.adapters.LastMoviesAdapter
import com.example.movieapp.ui.movies.AllMovieFragment
import com.example.movieapp.utils.CheckConnection
import com.example.movieapp.utils.MyResponse
import com.example.movieapp.utils.initRecycler
import com.example.movieapp.utils.isVisible
import com.example.movieapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var searchAdapter: LastMoviesAdapter

    @Inject
    lateinit var connection: CheckConnection

    enum class PageState {
        EMPTY,
        NETWORK,
        SERVER,
        NONE
    }

    //Other
    private val viewModel: SearchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {


            //Search
            searchEdt.addTextChangedListener {

                viewModel.loadSearch(it.toString())

            }
            //get movies list
            viewModel.moviesList.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        searchLoading.isVisible(true, moviesRecycler)
                    }

                    MyResponse.Status.SUCCESS -> {
                        searchLoading.isVisible(false, moviesRecycler)
                        if (it.data?.data != null) {
                            if (it.data.data.isNotEmpty()) {
                                checkConnectionOrEmpty(false, PageState.NONE)
                                searchAdapter.setData(it.data.data)
                                moviesRecycler.initRecycler(
                                    LinearLayoutManager(requireContext()),
                                    searchAdapter
                                )
                            } else {
                                checkConnectionOrEmpty(true, PageState.EMPTY)

                            }
                        }


                    }

                    MyResponse.Status.ERROR -> {

                        searchLoading.isVisible(false, moviesRecycler)
                        checkConnectionOrEmpty(true, PageState.SERVER)


                    }
                }

            }
            //Click
            searchAdapter.setOnItemClickListener {
                val direction = SearchFragmentDirections.actionToDetail(it.id!!.toInt())
                findNavController().navigate(direction)
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
                homeDisLay.isVisible(true, moviesRecycler)
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
                homeDisLay.isVisible(false, moviesRecycler)

            }
        }

    }


}