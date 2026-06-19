package com.ecotravel.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * representa un viaje con todos sus datos relacionados al CO2
 * implementa Parcelable para poder ser pasado entre Activities vía Intent.
 *
 * @param destination     Nombre de la ciudad/lugar de destino.
 * @param distanceKm      Distancia recorrida en kilómetros.
 * @param transportType   Tipo de transporte elegido por el usuario (PLANE, CAR, TRAIN).
 * @param isBusinessTrip  Indica si debe aplicarse el recargo por viaje de negocios (15%).
 * @param co2Kg           Emisiones de CO2 calculadas en kg (ya incluye el recargo si corresponde).
 * @param emissionFactor  Factor de emisión (g CO2/km) usado para el transporte elegido.
 */
@Parcelize
data class TripData(
    val destination: String,
    val distanceKm: Double,
    val transportType: TransportType,
    val isBusinessTrip: Boolean,
    val co2Kg: Double,
    val emissionFactor: Double
) : Parcelable

/**
 * enum class que representa las tres opciones de transporte (avion,auto y tren)
 * cada uno tiene su consumo de CO2 en gramos por kilometro
 */
enum class TransportType(val emissionFactorGPerKm: Double, val label: String) {
    PLANE(255.0, "Avion"),
    CAR(171.0, "Auto"),
    TRAIN(41.0, "Tren")
}
