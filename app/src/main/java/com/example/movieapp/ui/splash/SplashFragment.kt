package com.example.movieapp.ui.splash

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentSplashBinding
import com.example.movieapp.utils.StoreUserData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    //Binding
    private lateinit var binding: FragmentSplashBinding


    @Inject
    lateinit var storeUserData: StoreUserData


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)  // Use view binding bind() method

        // Set delay using lifecycleScope
        lifecycle.coroutineScope.launchWhenCreated {
            delay(1500)
            //Check user token
            storeUserData.getUserToken().collect {
                var destinationId = 0
                if (it.isEmpty()) {
                    findNavController().navigate(R.id.actionSplashToRegister)
                } else {
                        destinationId = R.id.actionToHome
                }
                if (R.id.actionToHome == destinationId){
                    findNavController().popBackStack()
                    findNavController().navigate(destinationId)
                }
            }

        }
    }

}
