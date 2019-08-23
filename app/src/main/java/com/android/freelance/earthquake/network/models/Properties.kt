package com.android.freelance.earthquake.network.models

import com.google.gson.annotations.SerializedName


/*import com.google.gson.annotations.SerializedName*/

data class Properties(

    @SerializedName("mag")
    val mag: Double,

    @SerializedName("place")
    val place: String,

    @SerializedName("time")
    val time: Long,

    @SerializedName("url")
    val url: String

    /*val updated: Long,
    val tz: Int,
    val detail: String,
    val felt: Int,
    val cdi: Double,
    val mmi: Double,
    val alert: String,
    val status: String,
    val tsunami: Int,
    val sig: Int,
    val net: String,
    val code: String,
    val ids: String,
    val sources: String,
    val types: String,
    val nst: Any,
    val dmin: Double,
    val rms: Double,
    val gap: Int,
    val magType: String,
    val type: String,
    val title: String*/
)