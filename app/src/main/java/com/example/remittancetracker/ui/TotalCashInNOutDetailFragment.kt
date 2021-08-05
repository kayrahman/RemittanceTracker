package com.example.remittancetracker.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.remittancetracker.R

class TotalCashInNOutDetailFragment : Fragment() {

    companion object {
        fun newInstance() = TotalCashInNOutDetailFragment()
    }

    private lateinit var viewModel: TotalCashInNOutDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.total_cash_in_n_out_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TotalCashInNOutDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}