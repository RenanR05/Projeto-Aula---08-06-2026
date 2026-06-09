package com.univali.geolocation.data

import com.univali.geolocation.domain.model.LocationData
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
class IOSLocationService : LocationService {

    private val locationManager = CLLocationManager().apply {
        desiredAccuracy = kCLLocationAccuracyBest
    }

    override fun observeLocation(): Flow<LocationData> = callbackFlow {
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
                trySend(
                    LocationData(
                        latitude = location.coordinate.useContents { latitude },
                        longitude = location.coordinate.useContents { longitude },
                        accuracy = location.horizontalAccuracy.toFloat().takeIf { it >= 0 },
                        timestamp = (location.timestamp.timeIntervalSince1970 * 1000).toLong()
                    )
                )
            }
            
            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                close(Exception(didFailWithError.localizedDescription))
            }
        }

        locationManager.delegate = delegate
        locationManager.startUpdatingLocation()

        awaitClose {
            locationManager.stopUpdatingLocation()
            locationManager.delegate = null
        }
    }

    override suspend fun getCurrentLocation(): LocationData = suspendCancellableCoroutine { continuation ->
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                val location = didUpdateLocations.lastOrNull() as? CLLocation
                if (location != null) {
                    locationManager.stopUpdatingLocation()
                    locationManager.delegate = null
                    
                    continuation.resume(
                        LocationData(
                            latitude = location.coordinate.useContents { latitude },
                            longitude = location.coordinate.useContents { longitude },
                            accuracy = location.horizontalAccuracy.toFloat().takeIf { it >= 0 },
                            timestamp = (location.timestamp.timeIntervalSince1970 * 1000).toLong()
                        )
                    )
                }
            }

            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                locationManager.stopUpdatingLocation()
                locationManager.delegate = null
                continuation.resumeWithException(Exception(didFailWithError.localizedDescription))
            }
        }

        locationManager.delegate = delegate
        locationManager.requestLocation()
        
        continuation.invokeOnCancellation {
            locationManager.stopUpdatingLocation()
            locationManager.delegate = null
        }
    }
}
