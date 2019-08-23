package com.android.freelance.earthquake.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.freelance.earthquake.R
import com.android.freelance.earthquake.adapter.EarthquakeAdapter
import com.android.freelance.earthquake.network.RetrofitFactory
import com.android.freelance.earthquake.network.models.EarthquakeResponse
import com.android.freelance.earthquake.network.models.Features
import kotlinx.android.synthetic.main.activity_earthquake.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import retrofit2.HttpException

class EarthquakeActivity : AppCompatActivity(), EarthquakeAdapter.ListItemClickListener {

    private val LOG_TAG = EarthquakeActivity::class.java.name

    var progressBar: ProgressBar? = null
    var hasInternet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(LOG_TAG, "TEST: onCreate() is called...")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earthquake)

        progressBar = findViewById(R.id.pbLoadingIndicator)
        progressBarLoading()

        callWebService()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun callWebService() {
        Log.i(LOG_TAG, "TEST: callWebService() is called...")

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val minMagnitude = sharedPrefs.getString(
            getString(R.string.settings_min_magnitude_key),
            getString(R.string.settings_min_magnitude_default)
        )

        val orderBy = sharedPrefs.getString(
            getString(R.string.settings_order_by_key),
            getString(R.string.settings_order_by_default)
        )

        val service = RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {

            val result = service.getEarthquakeList(
                getString(R.string.format),
                getString(R.string.eventtype),
                orderBy,
                Integer.parseInt(minMagnitude),
                10
            )

            try {
                withContext(Dispatchers.Main) {
                    if (result.isSuccessful && isNetworkAvailable()) {
                        hasInternet = true
                        result.body()?.let {
                            refreshUIWith(it.features)
                        }
                        progressBarGone()
                    } else {
                        toast("Error network operation failed with ${result.code()}")
                        progressBarGone()
                    }
                }
            } catch (e: HttpException) {
                Log.e("REQUEST", "Exception ${e.message}")
            } catch (e: Throwable) {
                Log.e("REQUEST", "Ooops: Something else went wrong")
            }
        }
    }

    private fun refreshUIWith(featuresList: List<Features>) {
        Log.i(LOG_TAG, "TEST: refreshUIWith() is called...")

        val earthquakeList = rvEarthquakeList
        val layoutManager = LinearLayoutManager(this)
        earthquakeList.layoutManager = layoutManager
        earthquakeList.hasFixedSize()
        earthquakeList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        val adapter = EarthquakeAdapter(applicationContext, featuresList, this@EarthquakeActivity)
        earthquakeList.adapter = adapter
    }

    override fun onListItemClick(clickItemIndex: Int, featuresList: List<Features>) {
        Log.i(LOG_TAG, "TEST: onListItemClick() is called...")

        val url = featuresList[clickItemIndex].properties.url
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(i)
    }

    private fun isNetworkAvailable(): Boolean {
        Log.i(LOG_TAG, "TEST: isNetworkAvailable() is called...")

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun progressBarLoading() {
        Log.i(LOG_TAG, "TEST: progressBarLoading() is called...")

        // when the task is started, make progressBar is loading
        progressBar?.visibility = View.VISIBLE
    }

    private fun progressBarGone() {
        Log.i(LOG_TAG, "TEST: progressBarLoading() is called...")

        // when the task is completed, make progressBar gone
        progressBar?.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.i(LOG_TAG, "TEST : onCreateOptionsMenu() called...")

        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
