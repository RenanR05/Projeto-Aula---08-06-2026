package com.univali.geolocation.data

import com.univali.geolocation.domain.model.LocationData
import com.univali.geolocation.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val locationService: LocationService
) : LocationRepository {
    override fun observeLocation(): Flow<LocationData> {
        return locationService.observeLocation()
    }

    override suspend fun getCurrentLocation(): LocationData {
        return locationService.getCurrentLocation()
    }
}
