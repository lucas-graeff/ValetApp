package lucas.graeff.valetwithmaps

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lucas.graeff.valetwithmaps.RecyclerView.CustomerAdapter
import lucas.graeff.valetwithmaps.database.AppDatabase
import lucas.graeff.valetwithmaps.database.Car
import lucas.graeff.valetwithmaps.databinding.ActivityMapsBinding
import kotlin.concurrent.thread

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Create database
        var db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "cars"
        ).build()

//        //Find database
//        var db: AppDatabase = AppDatabase.getInstance(this)
//
//        if(db == null) {
//            //Create database
//            db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java, "cars"
//            ).build()
//        }

        //TODO: Only show map if there is a vehicle parked
        val map: View = findViewById(R.id.map)
        thread {
            if(db.carDao().getParkedCars().isNotEmpty()) {
                map.visibility = View.VISIBLE
            }
        }

        //Ask for location permissions
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MapsActivity,
                    ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MapsActivity,
                    arrayOf(ACCESS_FINE_LOCATION), 1
                )
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        thread {
            val data = db.carDao().getMyCars()
            //RecyclerView
            val recyclerView: RecyclerView = findViewById(R.id.recycler_cars)
            val customerAdapter = CustomerAdapter(data, this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = customerAdapter
        }

        //Button listener
        val addBtn: ImageButton = findViewById(R.id.btn_add_car)

        addBtn.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        //Button listener
        val employeeBtn: Button = findViewById(R.id.btn_employee)

        employeeBtn.setOnClickListener {
            val intent = Intent(this, EmployeeActivity::class.java)
            startActivity(intent)
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Get lat and long from db of all user's cars
        val db: AppDatabase = AppDatabase.getInstance(this)
        thread {
            val cars: List<Car> = db.carDao().getParkedCars()

            runOnUiThread {
                //For loop to create markers
                for (car in cars) {
                    // Add a marker for the car and move the camera
                    if (car.latitude != null && car.longitude != null) {
                        val location = LatLng(car.latitude, car.longitude)
                        mMap.addMarker(
                            MarkerOptions().position(location).title("${car.make} ${car.model}")
                        )
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                        mMap.isMyLocationEnabled = true
                    }
                }
            }
        }
    }

}