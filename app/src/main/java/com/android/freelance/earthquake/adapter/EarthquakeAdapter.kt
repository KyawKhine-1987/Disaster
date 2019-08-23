package com.android.freelance.earthquake.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.android.freelance.earthquake.R
import com.android.freelance.earthquake.network.models.Features

import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.floor

/**
 * Created by Kyaw Khine on 10/10/2017.
 */

class EarthquakeAdapter(private val context: Context, private val featuresList: List<Features>, private val mOnClickListener: ListItemClickListener) : RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder>() {

    companion object {

        private val LOG_TAG = EarthquakeAdapter::class.java.name

        /**
         * The part of the location string from the USGS service that we use to determine
         * whether or not there is a location offset present ("5km N of Cairo, Egypt").
         */
        private const val LOCATION_SEPARATOR = " of "
    }

    interface ListItemClickListener {
        fun onListItemClick(clickItemIndex: Int, featuresList: List<Features>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeViewHolder {
        Log.i(LOG_TAG, "TEST: onCreateViewHolder() called...")

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city_list, parent, false)
        return EarthquakeViewHolder(view)
    }

    override fun getItemCount(): Int = featuresList.size

    override fun onBindViewHolder(holder: EarthquakeViewHolder, position: Int) {
        Log.i(LOG_TAG, "TEST: onBindViewHolder()  called...")

        /*val magnitude = java.lang.Double.toString(featuresList[position].properties.mag)*/
        val magnitude = featuresList[position].properties.mag.toString()
        holder.mag.text = magnitude

        val magnitudeCircle = holder.mag.background as GradientDrawable
        val magnitudeColor = getMagnitudeColor(featuresList[position].properties.mag)
        magnitudeCircle.setColor(magnitudeColor)

        val originalLocation = featuresList[position].properties.place

        val locationOffset: String
        val primaryLocation: String

        if (originalLocation.contains(LOCATION_SEPARATOR)) {

            /*val parts = originalLocation.split(LOCATION_SEPARATOR.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()*/
            val parts = originalLocation.split(LOCATION_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            locationOffset = parts[0] + LOCATION_SEPARATOR

            primaryLocation = parts[1]
        } else {
            // Otherwise, there is no " of " text in the originalLocation string.
            // Hence, set the default location offset to say "Near the".
            locationOffset = context.getString(R.string.near_the)
            primaryLocation = originalLocation
        }

        holder.locationOffset.text = locationOffset
        holder.primaryLocation.text = primaryLocation

        val dateTimeObject = Date(featuresList[position].properties.time)

        val formattedDate = formatDate(dateTimeObject)
        holder.date.text = formattedDate

        val formattedTime = formatTime(dateTimeObject)
        holder.time.text = formattedTime
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    @SuppressLint("SimpleDateFormat")
    private fun formatDate(dateObject: Date): String {
        Log.i(LOG_TAG, "TEST: formatDate()  called...")

       /* val dateFormat = SimpleDateFormat("LLL dd, yyyy")*/
        val dateFormat = SimpleDateFormat("LLL dd, yyyy")
        return dateFormat.format(dateObject)
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    @SuppressLint("SimpleDateFormat")
    private fun formatTime(dateObject: Date): String {
        Log.i(LOG_TAG, "TEST: formatTime()  called...")

        val timeFormat = SimpleDateFormat("h:mm a")
        return timeFormat.format(dateObject)
    }

    private fun getMagnitudeColor(magnitude: Double): Int {
        Log.i(LOG_TAG, "TEST: getMagnitudeColor()  called...")

        val magnitudeColorResourceId: Int = when (floor(magnitude).toInt()) {
            0, 1 -> R.color.magnitude1
            2 -> R.color.magnitude2
            3 -> R.color.magnitude3
            4 -> R.color.magnitude4
            5 -> R.color.magnitude5
            6 -> R.color.magnitude6
            7 -> R.color.magnitude7
            8 -> R.color.magnitude8
            9 -> R.color.magnitude9
            else -> R.color.magnitude10plus
        }

        return ContextCompat.getColor(context, magnitudeColorResourceId)
        /*val magnitudeFloor = Math.floor(magnitude).toInt()*/
        /*val magnitudeFloor = floor(magnitude).toInt()
        when (magnitudeFloor) {*/
    }

    inner class EarthquakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val LOG_TAG = EarthquakeViewHolder::class.java.name

        val mag = itemView.findViewById<View>(R.id.tvMagnitude) as TextView
        val locationOffset: TextView = itemView.findViewById<View>(R.id.tvLocationOffset) as TextView
        val primaryLocation: TextView = itemView.findViewById<View>(R.id.tvPrimaryLocation) as TextView
        val date: TextView = itemView.findViewById<View>(R.id.tvDate) as TextView
        val time: TextView  = itemView.findViewById<View>(R.id.tvTime) as TextView

        init {

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.i(LOG_TAG, "TEST: onClick() called...")
            val clickedPosition = adapterPosition
            mOnClickListener.onListItemClick(clickedPosition, featuresList)
        }
    }


}
