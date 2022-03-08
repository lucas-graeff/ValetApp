package lucas.graeff.valetwithmaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import lucas.graeff.valetwithmaps.database.AppDatabase
import lucas.graeff.valetwithmaps.database.Car
import kotlin.concurrent.thread

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        //Button listener
        val createBtn: Button = findViewById(R.id.btn_create_car)

        createBtn.setOnClickListener {
            thread {
                createCar()
            }
        }
    }

    private fun createCar() {
        var textView: TextView = findViewById(R.id.input_Make)
        val make: String = textView.text.toString()
        textView = findViewById(R.id.input_Model)
        val model: String = textView.text.toString()
        textView = findViewById(R.id.input_Year)
        val year: Int = textView.text.toString().toInt()
        textView = findViewById(R.id.input_Plate)
        val plate: String = textView.text.toString()

        var db: AppDatabase = AppDatabase.getInstance(this)
        val userDao = db.carDao()
        val car: Car = Car(0, make, model, year, plate, 0, 0, 0, null, null)
        userDao.addCar(car)
    }
}