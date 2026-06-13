package com.example.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.LuxuryItem
import com.example.data.LuxuryItemRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

sealed interface DeliveryStatus {
    object Idle : DeliveryStatus
    object Locating : DeliveryStatus
    data class TransmittingLocation(val lat: Double, val lng: Double) : DeliveryStatus
    data class DriverDispatched(
        val clientLat: Double,
        val clientLng: Double,
        val driverLat: Double,
        val driverLng: Double,
        val speedMps: Double,
        val progressPercent: Float,
        val etaSeconds: Int,
        val routeStatus: String
    ) : DeliveryStatus
    object Completed : DeliveryStatus
}

data class LuxuryMarketUiState(
    val items: List<LuxuryItem> = emptyList(),
    val isFilterOnlyActive: Boolean = true,
    val selectedCategory: String = "All",
    val deliveryState: DeliveryStatus = DeliveryStatus.Idle,
    val activeListingCount: Int = 0,
    val archivedListingCount: Int = 0,
    val showAdminConsole: Boolean = false,
    val activePurchasedItem: LuxuryItem? = null
)

class LuxuryMarketViewModel(private val repository: LuxuryItemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LuxuryMarketUiState())
    val uiState: StateFlow<LuxuryMarketUiState> = _uiState.asStateFlow()

    private var deliverySimulationJob: Job? = null

    init {
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
            observeListings()
        }
    }

    private fun observeListings() {
        viewModelScope.launch {
            repository.getAllItems().collectLatest { allListings ->
                val now = System.currentTimeMillis()
                val cutoff = now - (24 * 60 * 60 * 1000)
                
                // Automatically sweep and delete any database entries older than 24 hours
                val expiredCount = allListings.count { it.timestamp < cutoff }
                if (expiredCount > 0) {
                    repository.deleteExpired(cutoff)
                    return@collectLatest
                }

                val activeListings = allListings.filter { it.timestamp >= cutoff }
                val filteredListings = activeListings.filter { item ->
                    _uiState.value.selectedCategory == "All" || item.materialCategory == _uiState.value.selectedCategory
                }

                _uiState.value = _uiState.value.copy(
                    items = filteredListings,
                    activeListingCount = activeListings.size,
                    archivedListingCount = 0
                )
            }
        }
    }

    fun toggleFilterOnlyActive() {
        _uiState.value = _uiState.value.copy(isFilterOnlyActive = !_uiState.value.isFilterOnlyActive)
    }

    fun setCategoryFilter(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun toggleAdminConsole() {
        _uiState.value = _uiState.value.copy(showAdminConsole = !_uiState.value.showAdminConsole)
    }

    fun triggerCronSweep() {
        viewModelScope.launch {
            val cutoff = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
            val deletedCount = repository.deleteExpired(cutoff)
            Log.d("LuxuryVM", "Manual Cron Sweep deleted $deletedCount items")
            // Re-trigger update
            observeListings()
        }
    }

    fun addNewItem(
        title: String,
        category: String,
        price: Double,
        description: String,
        phone: String,
        imageUrl: String,
        customAgeHours: Float = 0f
    ) {
        viewModelScope.launch {
            val ageMs = (customAgeHours * 60 * 60 * 1000).toLong()
            val item = LuxuryItem(
                title = title,
                description = description,
                price = price,
                imageUrl = imageUrl,
                sellerPhone = phone,
                timestamp = System.currentTimeMillis() - ageMs,
                materialCategory = category,
                latitude = 40.7128 + (Math.random() - 0.5) * 0.1,
                longitude = -74.0060 + (Math.random() - 0.5) * 0.1
            )
            repository.insert(item)
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    @SuppressLint("MissingPermission")
    fun initiatePurchaseAndTracking(context: Context, item: LuxuryItem) {
        deliverySimulationJob?.cancel()
        _uiState.value = _uiState.value.copy(
            deliveryState = DeliveryStatus.Locating,
            activePurchasedItem = item
        )

        viewModelScope.launch {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        startDeliveryStream(location.latitude, location.longitude, item)
                    } else {
                        // Fallback coordinates representing dynamic high-end client zone (e.g. Tribeca Manhattan)
                        val fallbackLat = 40.7182 + (Math.random() - 0.5) * 0.01
                        val fallbackLng = -74.0080 + (Math.random() - 0.5) * 0.01
                        startDeliveryStream(fallbackLat, fallbackLng, item)
                    }
                }.addOnFailureListener {
                    val fallbackLat = 40.7182 + (Math.random() - 0.5) * 0.01
                    val fallbackLng = -74.0080 + (Math.random() - 0.5) * 0.01
                    startDeliveryStream(fallbackLat, fallbackLng, item)
                }
            } catch (e: SecurityException) {
                // Return fallback coordinate if permission denied (graceful recovery matching standard guidelines)
                val fallbackLat = 40.7182 + (Math.random() - 0.5) * 0.01
                val fallbackLng = -74.0080 + (Math.random() - 0.5) * 0.01
                startDeliveryStream(fallbackLat, fallbackLng, item)
            }
        }
    }

    private fun startDeliveryStream(clientLat: Double, clientLng: Double, item: LuxuryItem) {
        _uiState.value = _uiState.value.copy(
            deliveryState = DeliveryStatus.TransmittingLocation(clientLat, clientLng)
        )

        // Starting dynamic high-fidelity logistics simulation (vehicle travels to target)
        deliverySimulationJob = viewModelScope.launch {
            delay(1500) // Telemetry signal link up latency simulation

            // Start driver location at an offset
            var driverLat = clientLat + 0.015
            var driverLng = clientLng - 0.012
            val totalSteps = 40
            val speedMps = 14.5 // ~52 km/h city transport speed

            for (step in 1..totalSteps) {
                val progress = step.toFloat() / totalSteps
                driverLat = driverLat + (clientLat - driverLat) * (1.0 / (totalSteps - step + 1))
                driverLng = driverLng + (clientLng - driverLng) * (1.0 / (totalSteps - step + 1))

                val remainingSteps = totalSteps - step
                val etaSeconds = remainingSteps * 2 // ~2 seconds per simulated interval

                val statusMessage = when {
                    step < 10 -> "Dispatching Transit Fleet..."
                    step < 20 -> "Loading Premium Material at Depot..."
                    step < 35 -> "Route Optimization Active - In Transit"
                    else -> "Arriving at Client Destination..."
                }

                _uiState.value = _uiState.value.copy(
                    deliveryState = DeliveryStatus.DriverDispatched(
                        clientLat = clientLat,
                        clientLng = clientLng,
                        driverLat = driverLat,
                        driverLng = driverLng,
                        speedMps = speedMps + (Math.random() - 0.5) * 4,
                        progressPercent = progress,
                        etaSeconds = etaSeconds,
                        routeStatus = statusMessage
                    )
                )
                delay(1200) // Telemetry clock update interval
            }

            _uiState.value = _uiState.value.copy(
                deliveryState = DeliveryStatus.Completed
            )
            delay(5000) // Hold completed state before auto-clearing
            resetDelivery()
        }
    }

    fun resetDelivery() {
        deliverySimulationJob?.cancel()
        _uiState.value = _uiState.value.copy(
            deliveryState = DeliveryStatus.Idle,
            activePurchasedItem = null
        )
    }

    override fun onCleared() {
        super.onCleared()
        deliverySimulationJob?.cancel()
    }
}

class LuxuryMarketViewModelFactory(private val repository: LuxuryItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LuxuryMarketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LuxuryMarketViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
