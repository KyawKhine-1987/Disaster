package com.android.freelance.earthquake.network.models

import com.google.gson.annotations.SerializedName


/*import com.google.gson.annotations.SerializedName*/

data class EarthquakeResponse(
    @SerializedName("features")
    val features: List<Features>
)