package com.example.remittancetracker.model

import android.os.Parcelable
import android.util.Log
import com.example.remittancetracker.util.USER_TYPE_AGENT
import com.nkr.bazarano.util.Helperutils.Companion.formattedDate
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
    val timestamp: Date = Date()
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
    val uid : String="",
    val username : String="",
    val email : String="",
    val user_type : String = ""
) : Parcelable