package com.example.remittancetracker.ui.authentication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.remittancetracker.R
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.databinding.AuthenticationFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.nkr.bazaranocustomer.base.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationFragment : BaseFragment() {

    private val viewModel: AuthenticationViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding: AuthenticationFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AuthenticationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        binding.btnLogin.setOnClickListener {
            signinWithEmailNpass()
        }

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> {
                    viewModel.getUserInfo()
                }
            }

        })


    }

    private fun signinWithEmailNpass() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            viewModel.showLoading.value = true

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    viewModel.showLoading.value = false
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                       // navigateToHomeScreen()

                    } else {
                        // If sign in fails, display a message to the user.
                        viewModel.showSnackBar.value = "Singin Unsuccessful"
                    }
                }
        }else{
            viewModel.showSnackBar.value = "Fields empty"
        }

    }




}