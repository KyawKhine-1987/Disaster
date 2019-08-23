package com.android.freelance.earthquake.network.models

import com.google.gson.annotations.SerializedName


/*import com.google.gson.annotations.SerializedName*/

data class Features(
    @SerializedName("properties")
    val properties: Properties
)