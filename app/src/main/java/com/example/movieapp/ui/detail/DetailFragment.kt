package com.example.movieapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDetailBinding
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.ui.home.HomeFragment
import com.example.movieapp.utils.CheckConnection
import com.example.movieapp.utils.MyResponse
import com.example.movieapp.utils.initRecycler
import com.example.movieapp.utils.isVisible
import com.example.movieapp.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentDetailBinding

    @Inject
    lateinit var imagesAdapter: ImagesAdapter

    @Inject
    lateinit var entity: MovieEntity

    @Inject
    lateinit var connection: CheckConnection

    //Other
    private var movieID = 0
    private var isFavorite = false
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
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
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Load data
            movieID = args.movieID
            //back
            backImg.setOnClickListener { findNavController().navigateUp() }
            //call api
            viewModel.loadDetailMovie(movieID)
            viewModel.detailMovie.observe(viewLifecycleOwner) { response ->
                when (response.status) {
                    MyResponse.Status.LOADING -> {
                        detailLoading.isVisible(true, detailScrollView)
                    }

                    MyResponse.Status.SUCCESS -> {
                        detailLoading.isVisible(false, detailScrollView)
                        movieNameTxt.text = response.data?.title
                        movieRateTxt.text = response.data?.imdbRating
                        movieTimeTxt.text = response.data?.runtime
                        movieDateTxt.text = response.data?.released
                        movieSummaryInfo.text = response.data?.plot
                        movieActorsInfo.text = response.data?.actors
                        //Images Adapter
                        imagesAdapter.differ.submitList(response.data?.images)
                        imagesRecyclerView.initRecycler(
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            ),
                            imagesAdapter
                        )

                        //Entity
                        entity.id = movieID
                        entity.poster = response.data?.poster.toString()
                        entity.title = response.data?.title.toString()
                        entity.rate = response.data?.rated.toString()
                        entity.country = response.data?.country.toString()
                        entity.year = response.data?.year.toString()


                    }

                    MyResponse.Status.ERROR -> {
                        detailLoading.isVisible(false, detailScrollView)
                        checkConnectionOrEmpty(true, PageState.SERVER)

                    }

                }
                posterBigImg.load(response.data?.poster)
                posterNormalImg.load(response.data?.poster) {
                    crossfade(true)
                    crossfade(800)
                }

            }
            //fav
            viewModel.existsMovie(movieID)
            viewModel.isFavorite.observe(viewLifecycleOwner) {
                isFavorite = it
                if (it) {
                    favImg.setColorFilter(ContextCompat.getColor(requireContext(), R.color.scarlet))
                } else {
                    favImg.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.philippineSilver
                        )
                    )

                }

            }

            favImg.setOnClickListener {
                if (isFavorite) {
                    viewModel.deleteMovie(entity)
                } else
                    viewModel.saveMovie(entity)
            }

            //Back
            backImg.setOnClickListener {
                findNavController().navigateUp()
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
                homeDisLay.isVisible(true, detailScrollView)
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

                homeDisLay.isVisible(false, detailScrollView)

            }
        }


    }

}
