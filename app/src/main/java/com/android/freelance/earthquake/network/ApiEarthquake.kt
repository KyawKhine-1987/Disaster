package com.android.freelance.earthquake.network

import com.android.freelance.earthquake.network.models.EarthquakeResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/*http://earthquake.usgs.gov/
fdsnws/event/1/query
?format=geojson
&eventtype=earthquake
&orderby=time
&minmag=6
&limit=10*/
interface ApiEarthquake {
    @GET("fdsnws/event/1/query")
    /*suspend fun getEarthquakeList(): Response<List<EarthquakeResponse>>*/
    suspend fun getEarthquakeList(@Query("format") format: String, @Query("eventtype") eventtype: String,
                          @Query("orderby") orderby: String, @Query("minmag") minmag: Int,
                          @Query("limit") limit: Int): Response<EarthquakeResponse>
}