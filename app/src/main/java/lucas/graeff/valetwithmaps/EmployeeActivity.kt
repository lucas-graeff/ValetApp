package lucas.graeff.valetwithmaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lucas.graeff.valetwithmaps.RecyclerView.CustomerAdapter
import lucas.graeff.valetwithmaps.RecyclerView.EmployeeAdapter
import lucas.graeff.valetwithmaps.database.AppDatabase
import lucas.graeff.valetwithmaps.fragment.AddFragment
import lucas.graeff.valetwithmaps.fragment.CurrentCarFragment
import kotlin.concurrent.thread

class EmployeeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        //Set fragment
        val addFragment = AddFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, addFragment)
            commit()
        }

        //Find database
        var db: AppDatabase = AppDatabase.getInstance(this)
        thread {
            val data = db.carDao().getCalledCars()
            //RecyclerView
            val recyclerView: RecyclerView = findViewById(R.id.recycler_requested)
            val employeeAdapter: EmployeeAdapter = EmployeeAdapter(data, this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = employeeAdapter
        }

    }
}