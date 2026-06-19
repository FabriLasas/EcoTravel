package com.ecotravel.app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ecotravel.app.EcoTravelApplication
import com.ecotravel.app.R
import com.ecotravel.app.data.SharedPreferencesManager
import com.ecotravel.app.databinding.ActivityTripConfigBinding
import com.ecotravel.app.model.TransportType
import com.ecotravel.app.viewmodel.TripViewModel

class DetalleViajeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripConfigBinding

    private val viewModel: TripViewModel
        get() = (application as EcoTravelApplication).tripViewModel

    // ayuda a la persistencia de los datos
    private lateinit var prefsManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTripConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = SharedPreferencesManager(this)

        displayPersistedData()
        setupClickListeners()
    }

    /** muestra la distancia total y el destino anterior del shredpreference */
    private fun displayPersistedData() {
        val totalKm = prefsManager.getTotalDistance()
        val lastDest = prefsManager.getLastDestination()

        binding.tvTotalDistancePersisted.text =
            getString(R.string.label_total_distance, totalKm)

        binding.tvLastDestination.text = if (lastDest != null)
            getString(R.string.label_last_destination, lastDest)
        else
            getString(R.string.label_no_trips_yet)
    }

    private fun setupClickListeners() {
        // boton para ir a la calculadora
        binding.btnCalculate.setOnClickListener {
            if (validateInput()) {
                navigateToResult()
            }
        }

        // boton para pasar al historial
        binding.btnViewHistory.setOnClickListener {
            startActivity(Intent(this, HistorialViajesActivity::class.java))
        }
    }

    /**
     * valida el destino y la distancia ingresada por el usuario
     * muestra un mensaje de error si alguno de los input no son validos
     *
     * @return true si todo es valido sino false
     */
    private fun validateInput(): Boolean {
        val destination = binding.etDestination.text.toString().trim()
        val distanceText = binding.etDistance.text.toString().trim()

        if (destination.isEmpty()) {
            binding.etDestination.error = getString(R.string.error_empty_destination)
            binding.etDestination.requestFocus()
            return false
        }

        // distancia tiene que ser un numero valido
        val distance = distanceText.toDoubleOrNull()
        if (distance == null || distance <= 0) {
            binding.etDistance.error = getString(R.string.error_invalid_distance)
            binding.etDistance.requestFocus()
            return false
        }

        return true
    }

    /**
     * lee los valores y salta el viewmodel para calcular los datos
     * le pasa los datos a la siguiente actividad
     */
    private fun navigateToResult() {
        val destination = binding.etDestination.text.toString().trim()
        val distanceKm = binding.etDistance.text.toString().toDouble()
        val isBusinessTrip = binding.cbBusinessTrip.isChecked

        val transportType = when (binding.rgTransport.checkedRadioButtonId) {
            R.id.rbCar   -> TransportType.CAR
            R.id.rbTrain -> TransportType.TRAIN
            else          -> TransportType.PLANE // default / rbPlane
        }

        // le pide al viewmodel que calcule los datos y los guarde
        val trip = viewModel.calculateAndAddTrip(
            destination, distanceKm, transportType, isBusinessTrip
        )

        // persistencia de los datos con sharedpreferences
        prefsManager.saveTotalDistance(viewModel.calculateTotalDistance())
        prefsManager.saveLastDestination(destination)

        // pasa los datos del viaje a la siguiente actividad
        val intent = Intent(this, ResultadosActivity::class.java).apply {
            putExtra(ResultadosActivity.EXTRA_TRIP_DATA, trip)
        }
        startActivity(intent)
    }
}
