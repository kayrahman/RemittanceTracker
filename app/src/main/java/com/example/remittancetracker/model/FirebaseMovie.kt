package com.example.remittancetracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList


@Parcelize
data class Movie(
    var uid: String,
    val video_url: String,
    val video_ref:String?,
    val img_url: String,
    val movie_title: String,
    val movie_year: String,
    val description: String,
    val type : String,
):Parcelable


@Parcelize
data class FirebaseUserInfo(
    val uid: String = "",
    val user_name: String = "",
    val email: String = "",
    val img_url: String = "",
    val subscription_plan:String = "",
    val user_type : Int = 0,
    val registration_tokens: ArrayList<String> = arrayListOf<String>()
) : Parcelable

class UserInfo(
    val user_name: String = "",
    val phone_number : String = ""
)



data class MovieLocationInfo(
    val video_url: String,
    val video_ref: String
)



data class FirebaseKeyword(
    val product_uid: String = "",
    val keyword: String = ""
)
