package com.example.greenish.SearchpageANDMypage
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterOptions(
    var managedemanddoCode: String = "",
    var winterLwetTpCodeNm: String = "",
    var lighttdemanddoCodeNm: String = "",
    var flclrCodeNm: String = "",
    var ignSeasonCodeNm: String = "",
    var grwhstleCodeNm: String = "",
    var lefmrkCodeNm: String = "",
    var fruit: String = ""
) : Parcelable