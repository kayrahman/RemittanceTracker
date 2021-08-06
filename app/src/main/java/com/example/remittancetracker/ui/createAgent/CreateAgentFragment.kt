package com.example.remittancetracker.ui.createAgent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.remittancetracker.R
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.databinding.CreateAgentFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAgentFragment : BaseFragment() {

    private val viewModel: CreateAgentViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding : CreateAgentFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = CreateAgentFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}