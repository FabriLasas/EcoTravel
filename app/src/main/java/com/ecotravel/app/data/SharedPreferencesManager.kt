package com.ecotravel.app.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Centraliza todas las operaciones del sharedpreference
 * mantiene los datos fuera de los viewmodel y activities
 */
class SharedPreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_FILE = "ecotravel_prefs"

        // claves
        private const val KEY_TOTAL_DISTANCE = "total_distance_km"
        private const val KEY_LAST_DESTINATION = "last_destination"
    }

    /** guarda sobreescribiendo la distancia total acumulada */
    fun saveTotalDistance(totalKm: Double) {
        prefs.edit { putFloat(KEY_TOTAL_DISTANCE, totalKm.toFloat()) }
    }

    /** recupera la distancia total y devuelve 0 si no hay ninguna. */
    fun getTotalDistance(): Double =
        prefs.getFloat(KEY_TOTAL_DISTANCE, 0f).toDouble()

    /** guarda el nombre del ultimo destino */
    fun saveLastDestination(destination: String) {
        prefs.edit { putString(KEY_LAST_DESTINATION, destination) }
    }

    /** recupera el ultimo destino, si no hay devuelve null */
    fun getLastDestination(): String? =
        prefs.getString(KEY_LAST_DESTINATION, null)
}
