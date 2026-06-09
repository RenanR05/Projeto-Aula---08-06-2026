package com.univali.geolocation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.univali.geolocation.data.AndroidLocationService
import com.univali.geolocation.data.LocationRepositoryImpl
import com.univali.geolocation.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Injeção manual de dependência
        val locationService = AndroidLocationService(applicationContext)
        val locationRepository = LocationRepositoryImpl(locationService)
        
        setContent {
            App(locationRepository = locationRepository)
        }
    }
}
