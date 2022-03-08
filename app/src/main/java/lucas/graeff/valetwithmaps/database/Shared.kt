package lucas.graeff.valetwithmaps.database

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.concurrent.thread

class Shared {
    companion object {
        private lateinit var fusedLocationClient: FusedLocationProviderClient

        @SuppressLint("MissingPermission")
        @JvmStatic
        fun setLocation(id: Int, context: Context) {
            //Find database
            var db: AppDatabase = AppDatabase.getInstance(context)

            //Get and set car's long and lat
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        Log.d("Test", "${location.latitude} , ${location.longitude}")
                        thread {
                            db.carDao().carLocation(location.longitude, location.latitude, id)
                        }
                    }
                }
        }
    }

}