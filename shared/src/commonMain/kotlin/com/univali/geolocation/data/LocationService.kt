package com.univali.geolocation.data

import com.univali.geolocation.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun observeLocation(): Flow<LocationData>
    suspend fun getCurrentLocation(): LocationData
}
