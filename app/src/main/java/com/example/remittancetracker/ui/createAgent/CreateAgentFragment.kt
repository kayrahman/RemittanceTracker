package com.example.remittancetracker.ui.createAgent

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
import com.example.remittancetracker.databinding.CreateAgentFragmentBinding
import com.example.remittancetracker.model.FirebaseUserInfo
import com.example.remittancetracker.ui.authentication.AuthenticationViewModel
import com.example.remittancetracker.util.USER_TYPE_AGENT
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAgentFragment : BaseFragment() {

    private val viewModel: CreateAgentViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding : CreateAgentFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = CreateAgentFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnCreateAgent.setOnClickListener {
            createUser()
        }


        viewModel.isAgentInfoUploadSuccessful.observe(viewLifecycleOwner, Observer {
            if(it){
                clearFields()
            }
        })
    }

    private fun clearFields() {
        binding.etUserName.setText("")
        binding.etEmail.setText("")
        binding.etPassword.setText("")
    }

    private fun createUser() {
        val username = binding.etUserName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if(email.isNotEmpty() && password.isNotEmpty()){
            viewModel.showLoading.value = true
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                viewModel.showLoading.value = false
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    val user = FirebaseUserInfo(uid = uid.toString(),username= username, email = email,user_type = USER_TYPE_AGENT)
                    viewModel.updateUserInfo(user)

                } else {
                    // If sign in fails, display a message to the user.
                    viewModel.showSnackBar.value = "Something went wrong"
                }
            }
        }else{
            viewModel.showSnackBar.value = "Fields Empty"
        }

    }

}