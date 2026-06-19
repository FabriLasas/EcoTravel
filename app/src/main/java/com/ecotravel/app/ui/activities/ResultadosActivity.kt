package com.ecotravel.app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ecotravel.app.R
import com.ecotravel.app.databinding.ActivityImpactResultBinding
import com.ecotravel.app.model.TransportType
import com.ecotravel.app.model.TripData

class ResultadosActivity : AppCompatActivity() {

    // linkea con el xml
    private lateinit var binding: ActivityImpactResultBinding

    companion object {
        const val EXTRA_TRIP_DATA = "extra_trip_data"
    }

    //crea el viaje
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImpactResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the Parcelable TripData from the Intent
        val trip: TripData? = intent.getParcelableExtra(EXTRA_TRIP_DATA)

        if (trip != null) {
            displayResult(trip)
        } else {
            finish() // Safety: should never happen in normal flow
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnViewHistory.setOnClickListener {
            startActivity(Intent(this, HistorialViajesActivity::class.java))
        }
    }

    /**
     * filtra los textviews para mostrar los datos del viaje
     * badado en el color de fondo del cardview
     */
    private fun displayResult(trip: TripData) {
        binding.tvResultDestination.text = trip.destination
        binding.tvResultTransport.text = trip.transportType.label
        binding.tvResultFactor.text = getString(
            R.string.label_emission_factor,
            trip.emissionFactor
        )
        binding.tvResultCo2.text = getString(
            R.string.label_total_co2,
            trip.co2Kg
        )

        // anotacion de viaje de negocios
        if (trip.isBusinessTrip) {
            binding.tvBusinessSurcharge.visibility = android.view.View.VISIBLE
            binding.tvBusinessSurcharge.text = getString(R.string.label_business_surcharge)
        } else {
            binding.tvBusinessSurcharge.visibility = android.view.View.GONE
        }

        // tema dinamico segun el tipo de transporte
        applyTransportTheme(trip.transportType)
    }

    /**
     * cambia el color de fondo y emoji segun el tipo de transporte
     */
    private fun applyTransportTheme(transportType: TransportType) {
        val (colorRes, emoji) = when (transportType) {
            TransportType.PLANE -> Pair(R.color.plane_blue, "✈️")
            TransportType.CAR   -> Pair(R.color.car_orange, "🚗")
            TransportType.TRAIN -> Pair(R.color.train_green, "🚆")
        }

        binding.cardResultHeader.setCardBackgroundColor(
            ContextCompat.getColor(this, colorRes)
        )
        binding.tvTransportIcon.text = emoji
    }
}
