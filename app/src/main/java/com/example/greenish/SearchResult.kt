package com.example.greenish

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResult(
    val cntntsNo: String,
    val cntntsSj: String,
    val rtnFileUrl: String,
    val fmlCodeNm: String,
    val clCodeNm: String

) : Parcelable