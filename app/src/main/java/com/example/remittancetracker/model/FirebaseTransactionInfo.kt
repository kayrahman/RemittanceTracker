package com.example.remittancetracker.model

import java.util.*


data class FirebaseTransactionInfo(
    val uid : String = "",
    val transaction_type : String = "",
    val payment_type : String = "",
    val account_name : String = "",
    val account_number : String = "",
    val amount : Int = 0,
    val city : String = "",
    val creator : String = "",
    val timestamp : Date = Date()
)