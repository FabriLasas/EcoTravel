package com.ecotravel.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.ecotravel.app.viewmodel.TripViewModel

/**
 * view model store
 * cada activity contiene su propio viewmodelstore, de ser tratado asi no se podria comunicar
 * la actovity 1 con la 3 del historial. En cambio se trabaja a nivel global donde
 * todo sea guardado en el mismo lugar y se pueda acceder
 */
class EcoTravelApplication : Application(), ViewModelStoreOwner {

    // 1 solo store para toda la app
    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }

    override val viewModelStore: ViewModelStore
        get() = appViewModelStore

    // lazy no le asigna el valor directamente, se crea el tripviewmodel y cuando se usa se guarda
    val tripViewModel: TripViewModel by lazy {
        ViewModelProvider(this)[TripViewModel::class.java]
    }
}
