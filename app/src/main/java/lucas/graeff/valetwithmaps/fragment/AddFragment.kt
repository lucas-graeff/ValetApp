package lucas.graeff.valetwithmaps.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import lucas.graeff.valetwithmaps.R
import lucas.graeff.valetwithmaps.database.AppDatabase
import kotlin.concurrent.thread

class AddFragment : Fragment(R.layout.fragment_add) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Button listener
        val addBtn: ImageButton = view.findViewById(R.id.btn_add_plate)
        val plateView: TextView = view.findViewById(R.id.input_employee_plate)

        addBtn.setOnClickListener {
            val plate = plateView.text.toString()
            var db: AppDatabase = AppDatabase.getInstance(view.context)

            thread {
                //Get car and set to be in possession and moving
                val car = db.carDao().getCarByPlate(plate)
                db.carDao().carPossessed(1, car.id)
                db.carDao().carMoving(1, car.id)

                //Create bundle
                val currentCarFragment = CurrentCarFragment()
                val bundle = Bundle()
                bundle.putString("make", "${car.year} ${car.make}")
                bundle.putString("model", car.model)
                bundle.putString("plate", car.plate)
                bundle.putInt("id", car.id)
                currentCarFragment.arguments = bundle

                //Change fragment
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_fragment, currentCarFragment)
                    commit()
                }
            }
        }
    }

}
