# 🌿 EcoTravel — Calculadora de Huella de Carbono

Aplicación Android desarrollada en **Kotlin** para el Segundo Examen de Desarrollo de Aplicaciones Android.

> 📹 **[VIDEO DE DEMOSTRACIÓN]** — *Insertar aquí el enlace al video una vez grabado.*

---

## Funcionalidades

| Pantalla | Descripción |
|---|---|
| **Activity 1 – Configuración de Viaje** | Ingreso de destino y distancia, selección de transporte (RadioGroup), checkbox de viaje de negocios (+15%). Muestra km totales y último destino persistidos. |
| **Activity 2 – Resultado de Impacto** | Muestra el CO₂ calculado con 2 decimales, el factor de emisión y el destino. Cambia colores dinámicamente según el transporte. |
| **Activity 3 – Historial de Rutas** | RecyclerView con todos los viajes. Tira de color lateral diferente por transporte. Long-press para eliminar con diálogo de confirmación. |

---

## Arquitectura: MVVM

```
┌─────────────────────────────────────────────────────────┐
│  UI Layer (Activities + Adapter)                        │
│  TripConfigActivity → ImpactResultActivity              │
│                      → TripHistoryActivity              │
│  TripAdapter (RecyclerView)                             │
└────────────────────┬────────────────────────────────────┘
                     │ observa LiveData / llama métodos
┌────────────────────▼────────────────────────────────────┐
│  ViewModel Layer                                        │
│  TripViewModel                                          │
│  • calculateAndAddTrip()  • removeTripAt()              │
│  • calculateTotalDistance()                             │
│  • LiveData<MutableList<TripData>>                      │
└────────────────────┬────────────────────────────────────┘
                     │ lee / escribe
┌────────────────────▼────────────────────────────────────┐
│  Data Layer                                             │
│  SharedPreferencesManager                               │
│  • total_distance_km    • last_destination              │
└─────────────────────────────────────────────────────────┘
```

---

## Factores de Emisión

| Transporte | Factor (g CO₂/km) |
|---|---|
| ✈️ Avión | 255 |
| 🚗 Auto | 171 |
| 🚆 Tren | 41 |

**Fórmula:**
```
CO₂ (kg) = distancia (km) × factor (g/km) ÷ 1000
Si es viaje de negocios: CO₂ × 1.15
```

---

## Requisitos Obligatorios Cumplidos

- ✅ **MVVM** con `ViewModel` + `LiveData`
- ✅ **RecyclerView** con diseño de ítem personalizado y tira de color por transporte
- ✅ **View Binding** en las 3 Activities y en el Adapter (sin `findViewById`)
- ✅ **SharedPreferences** — distancia total acumulada y último destino
- ✅ **Validación** — error en `EditText` si distancia ≤ 0 o destino vacío

## Puntos Extra Implementados

- ✅ **Cambio Temático Dinámico** — Activity 2 cambia color del card header (azul/naranja/verde)
- ✅ **Data Class Parcelable** — `TripData` con `@Parcelize` pasado entre Intents
- ✅ **Eliminación con long-click** — diálogo de confirmación antes de borrar
- ✅ **Persistencia del último destino** — guardado en SharedPreferences

---

## Estructura del Proyecto

```
app/src/main/java/com/ecotravel/app/
├── model/
│   └── TripData.kt          # Data class Parcelable + TransportType enum
├── data/
│   └── SharedPreferencesManager.kt  # Abstracción de persistencia
├── viewmodel/
│   └── TripViewModel.kt     # Lógica de negocio + LiveData
└── ui/
    ├── activities/
    │   ├── TripConfigActivity.kt    # Activity 1
    │   ├── ImpactResultActivity.kt  # Activity 2
    │   └── TripHistoryActivity.kt   # Activity 3
    └── adapters/
        └── TripAdapter.kt           # RecyclerView Adapter con View Binding
```

---

## Tecnologías

- Kotlin 1.9
- Android SDK 34 (min SDK 26)
- Material Components 1.12
- AndroidX Lifecycle (ViewModel + LiveData) 2.8
- View Binding
- Kotlin Parcelize plugin
