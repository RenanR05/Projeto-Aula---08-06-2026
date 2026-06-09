package com.univali.geolocation.domain.repository

import com.univali.geolocation.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun observeLocation(): Flow<LocationData>
    suspend fun getCurrentLocation(): LocationData
}
