package com.example.remittancetracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.remittancetracker.R
import com.example.remittancetracker.databinding.ListItemTransactionDetailBinding
import com.example.remittancetracker.model.FirebaseTransactionInfo
import com.nkr.bazarano.util.Helperutils.Companion.formattedDate


class TransactionListAdapter : ListAdapter<FirebaseTransactionInfo,TransactionListAdapter.TransactionViewHolder>(DiffCallBack())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding: ListItemTransactionDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_transaction_detail, parent, false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)


    }

    class TransactionViewHolder(val binding: ListItemTransactionDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FirebaseTransactionInfo) {
            binding.transactionInfo = item
            binding.executePendingBindings()
        }
    }


    class DiffCallBack : DiffUtil.ItemCallback<FirebaseTransactionInfo>() {
        override fun areItemsTheSame(oldItem: FirebaseTransactionInfo, newItem: FirebaseTransactionInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FirebaseTransactionInfo, newItem: FirebaseTransactionInfo): Boolean {
            return oldItem.uid == newItem.uid
        }
    }



}