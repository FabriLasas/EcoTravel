package com.ecotravel.app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ecotravel.app.R
import com.ecotravel.app.databinding.ItemTripBinding
import com.ecotravel.app.model.TransportType
import com.ecotravel.app.model.TripData

class TripAdapter(
    private val trips: MutableList<TripData>,
    private val onLongClickItem: (Int) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {


     // cada instancia se mapea en el historial de viajes
    inner class TripViewHolder(val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TripViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        with(holder.binding) {
            // llena los campos de texto
            tvItemDestination.text = trip.destination
            tvItemDistance.text = "${trip.distanceKm.toInt()} km"
            tvItemCo2.text = String.format("%.2f kg CO₂", trip.co2Kg)
            tvItemTransport.text = trip.transportType.label

            // color del costado de cada viaje segun el trasporte
            val accentColor = when (trip.transportType) {
                TransportType.PLANE -> ContextCompat.getColor(root.context, R.color.plane_blue)
                TransportType.CAR   -> ContextCompat.getColor(root.context, R.color.car_orange)
                TransportType.TRAIN -> ContextCompat.getColor(root.context, R.color.train_green)
            }
            viewTransportStrip.setBackgroundColor(accentColor)

            // emoji del transporte
            tvItemIcon.text = when (trip.transportType) {
                TransportType.PLANE -> "✈️"
                TransportType.CAR   -> "🚗"
                TransportType.TRAIN -> "🚆"
            }

            // etiqueta de viaje de negocios
            tvBusinessBadge.visibility = if (trip.isBusinessTrip)
                android.view.View.VISIBLE else android.view.View.GONE

            // listener para cuando mantienen apretado un viaje
            root.setOnLongClickListener {
                val position = holder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onLongClickItem(position)
                }
                true
            }
        }
    }

    override fun getItemCount(): Int = trips.size

    /** reemplaza la lista y notifica al adapter */
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: MutableList<TripData>) {
        trips.clear()
        trips.addAll(newList)
        notifyDataSetChanged()
    }
}
