package com.ecotravel.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecotravel.app.model.TransportType
import com.ecotravel.app.model.TripData

/**
 * ViewModel entre las tres Activities.
 * Mantiene el historial de viajes en memoria como LiveData
 * lógica de cálculo acá
 */
class TripViewModel : ViewModel() {

    private val _tripHistory = MutableLiveData<MutableList<TripData>>(mutableListOf())
    val tripHistory: LiveData<MutableList<TripData>> get() = _tripHistory

    private val BUSINESS_SURCHARGE = 0.15

    //calculadora de CO2
    fun calculateAndAddTrip(
        destination: String,
        distanceKm: Double,
        transportType: TransportType,
        isBusinessTrip: Boolean
    ): TripData {
        val factor = transportType.emissionFactorGPerKm
        val baseCo2Kg = (distanceKm * factor) / 1000.0

        // recargo por viaje de negocios
        val totalCo2Kg = if (isBusinessTrip) baseCo2Kg * (1 + BUSINESS_SURCHARGE) else baseCo2Kg

        val trip = TripData(
            destination = destination,
            distanceKm = distanceKm,
            transportType = transportType,
            isBusinessTrip = isBusinessTrip,
            co2Kg = totalCo2Kg,
            emissionFactor = factor
        )

        // agrega al historial
        val currentList = _tripHistory.value ?: mutableListOf()
        currentList.add(trip)
        _tripHistory.value = currentList

        return trip
    }

    /**
     * elimina un viaje de la lista
     * cuando se mantiene apretado un viaje
     */
    fun removeTripAt(position: Int) {
        val currentList = _tripHistory.value ?: return
        if (position in currentList.indices) {
            currentList.removeAt(position)
            // reasigna la lista al livedata
            _tripHistory.value = currentList
        }
    }

     // calcula la distancia total sumando todos los viajes registrados.
    fun calculateTotalDistance(): Double =
        _tripHistory.value?.sumOf { it.distanceKm } ?: 0.0
}
