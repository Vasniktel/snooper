package com.github.vasniktel.snooper.logic.location

import android.content.Context
import android.location.Geocoder
import androidx.annotation.RequiresPermission
import com.github.vasniktel.snooper.logic.model.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.resume

interface LocationProvider {
    suspend fun getLocation(): Location
}

class LocationProviderImpl(
    private val applicationContext: Context,
    private val locale: Locale
) : LocationProvider {
    private val provider by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    @RequiresPermission(anyOf = [
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION"
    ])
    override suspend fun getLocation(): Location {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine<Location> { cont ->
                val request = LocationRequest.create().apply {
                    interval = 3000
                    fastestInterval = 1000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    numUpdates = 1
                }

                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult?) {
                        result?.lastLocation?.let {
                            cont.resume(it.toModel())
                        }
                    }
                }

                try {
                    provider.requestLocationUpdates(request, callback, null)
                } catch (e: Exception) {
                    cont.cancel(e)
                }
            }
        }
    }

    private fun getAddress(location: android.location.Location): String {
        val address = Geocoder(applicationContext, locale)
            .getFromLocation(location.latitude, location.longitude, 1)[0]

        val city = address.locality!!
        val state = address.adminArea!!
        val country = address.countryName!!

        return "$city, $state, $country"
    }

    private fun android.location.Location.toModel() = Location(
        latitude = latitude,
        longitude = longitude,
        address = getAddress(this@toModel)
    )
}
