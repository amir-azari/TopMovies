package com.example.movieapp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentFavoriteBinding
import com.example.movieapp.ui.splash.SplashFragment
import com.example.movieapp.utils.initRecycler
import com.example.movieapp.utils.isVisible
import com.example.movieapp.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    //Binding
    private lateinit var binding : FragmentFavoriteBinding

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    //Other
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(inflater,container , false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Show all favorite
            viewModel.loadFavoriteList()
            //List
            viewModel.favoriteList.observe(viewLifecycleOwner) {
                if (it.isEmpty){
                    emptyItemsLay.isVisible(true,favoriteRecycler )

                }else{
                    emptyItemsLay.isVisible(false,favoriteRecycler )
                    favoriteAdapter.setData(it.data!!)
                    favoriteRecycler.initRecycler(LinearLayoutManager(requireContext()), favoriteAdapter)
                }

            }
            //Click
            favoriteAdapter.setOnItemClickListener {
                val direction = FavoriteFragmentDirections.actionToDetail(it.id)
                findNavController().navigate(direction)
            }


        }
    }
}
