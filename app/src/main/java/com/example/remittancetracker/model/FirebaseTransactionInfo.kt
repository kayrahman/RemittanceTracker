package com.example.remittancetracker.model

import android.os.Parcelable
import com.google.firebase.firestore.FieldValue
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*


data class FirebaseTransactionInfo(
    val uid: String = "",
    val transaction_type: String = "",
    val payment_type: String = "",
    val account_name: String = "",
    val account_number: String = "",
    val amount: Int = 0,
    val city: String = "",
    val creator: String = "",
    val timestamp: Date = Date(),
    val createdAt: Long = Date().time
) {

    fun getDate(): String {
        var date: String
        val format = SimpleDateFormat("dd.MM.yy")
        date = format.format(timestamp)

        return date
    }

}

@Parcelize
data class FirebaseUserInfo(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val user_type: String = "",
    val registration_tokens : ArrayList<String> = arrayListOf()
) : Parcelable


data class TransactionTotal(
    val total_sent_money: Int,
    val total_received_money: Int
)