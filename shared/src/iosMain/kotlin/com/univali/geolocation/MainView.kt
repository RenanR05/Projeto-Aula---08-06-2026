package com.univali.geolocation

import androidx.compose.ui.window.ComposeUIViewController
import com.univali.geolocation.data.IOSLocationService
import com.univali.geolocation.data.LocationRepositoryImpl
import com.univali.geolocation.ui.App

fun MainViewController() = ComposeUIViewController {
    val locationService = IOSLocationService()
    val locationRepository = LocationRepositoryImpl(locationService)
    
    App(locationRepository = locationRepository)
}
