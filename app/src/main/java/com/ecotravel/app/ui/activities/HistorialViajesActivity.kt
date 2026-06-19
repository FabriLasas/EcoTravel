package com.ecotravel.app.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecotravel.app.EcoTravelApplication
import com.ecotravel.app.R
import com.ecotravel.app.databinding.ActivityTripHistoryBinding
import com.ecotravel.app.ui.adapters.TripAdapter
import com.ecotravel.app.viewmodel.TripViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistorialViajesActivity : AppCompatActivity() {

    // link con el xml
    private lateinit var binding: ActivityTripHistoryBinding

    private val viewModel: TripViewModel
        get() = (application as EcoTravelApplication).tripViewModel

    private lateinit var tripAdapter: TripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        binding.btnBack.setOnClickListener { finish() }
    }

    /** inicializa el recycle view con el adapter y el layout manager */
    private fun setupRecyclerView() {
        tripAdapter = TripAdapter(
            trips = mutableListOf(),
            onLongClickItem = { position -> confirmDeletion(position) }
        )

        binding.rvTripHistory.apply {
            layoutManager = LinearLayoutManager(this@HistorialViajesActivity)
            adapter = tripAdapter
        }
    }

    /**
     * observa el historial del viewmodel
     * cuando la lista cambia el adapter se actualiza
     */
    private fun observeViewModel() {
        viewModel.tripHistory.observe(this) { trips ->
            tripAdapter.updateList(trips)

            // Toggle empty-state message
            if (trips.isEmpty()) {
                binding.tvEmptyHistory.visibility = android.view.View.VISIBLE
                binding.rvTripHistory.visibility = android.view.View.GONE
            } else {
                binding.tvEmptyHistory.visibility = android.view.View.GONE
                binding.rvTripHistory.visibility = android.view.View.VISIBLE
            }
        }
    }

    /**
     * muestra un mensaje de confiarmacion antes de borrar un viaje
     * llama ViewModel.removeTripAt() si el usuario confirma
     */
    private fun confirmDeletion(position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_message)
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.dialog_confirm_delete) { _, _ ->
                viewModel.removeTripAt(position)
            }
            .show()
    }
}
