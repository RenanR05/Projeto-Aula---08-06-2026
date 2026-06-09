package com.univali.geolocation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.isGranted
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import com.mohamedrejeb.calf.permissions.shouldShowRationale
import com.univali.geolocation.domain.repository.LocationRepository
import com.univali.geolocation.ui.dashboard.DashboardScreen
import com.univali.geolocation.ui.dashboard.DashboardViewModel
import com.univali.geolocation.ui.permission.PermissionScreen
import com.univali.geolocation.ui.theme.AppTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun App(locationRepository: LocationRepository) {
    AppTheme {
        val locationPermissionState = rememberPermissionState(Permission.FineLocation)
        
        if (locationPermissionState.status.isGranted) {
            // Permissão concedida, mostrar dashboard
            val viewModel = remember { DashboardViewModel(locationRepository) }
            DashboardScreen(viewModel)
        } else {
            // Permissão não concedida
            PermissionScreen(
                onRequestPermission = {
                    locationPermissionState.launchPermissionRequest()
                },
                isPermanentlyDenied = !locationPermissionState.status.shouldShowRationale && locationPermissionState.status is PermissionStatus.Denied,
                onOpenSettings = {
                    locationPermissionState.openAppSettings()
                }
            )
        }
    }
}
