package com.example.movieapp.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentRegisterBinding
import com.example.movieapp.models.register.BodyRegister
import com.example.movieapp.ui.splash.SplashFragment
import com.example.movieapp.utils.MyResponse
import com.example.movieapp.utils.StoreUserData
import com.example.movieapp.utils.isVisible
import com.example.movieapp.utils.showInvisible
import com.example.movieapp.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentRegisterBinding

    @Inject
    lateinit var userData: StoreUserData

    //Other
    private val viewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var bodyRegister: BodyRegister

    var flag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {

            viewModel.registerUser.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        submitLoading.showInvisible(true)
                        submitBtn.showInvisible(false)

                    }

                    MyResponse.Status.SUCCESS -> {
                        submitLoading.showInvisible(false)
                        submitBtn.showInvisible(true)
                        lifecycle.coroutineScope.launchWhenCreated {
                            userData.saveUserToken(it.data.toString())
                        }


                    }

                    MyResponse.Status.ERROR -> {
                        submitLoading.showInvisible(false)
                        submitBtn.showInvisible(true)
                        Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()


                    }
                }
            }
            //Click
            submitBtn.setOnClickListener { view ->
                val name = nameEdt.text.toString()
                val email = nemailEdt.text.toString()
                val password = passEdt.text.toString()
                //Validation
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    if (password.length < 8) {
                        Snackbar.make(
                            view,
                            "Password must be at least 8 characters long",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        bodyRegister.name = name
                        bodyRegister.email = email
                        bodyRegister.password = password
                    }
                } else {
                    Snackbar.make(view, getString(R.string.fillAllFields), Snackbar.LENGTH_SHORT)
                        .show()
                }
                //Send data
                viewModel.registerUsr(bodyRegister)

            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

                requireActivity().finish()
                }
        }
    }
}