package lucas.graeff.valetwithmaps.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import lucas.graeff.valetwithmaps.R
import lucas.graeff.valetwithmaps.database.AppDatabase
import lucas.graeff.valetwithmaps.database.Shared
import kotlin.concurrent.thread


class CurrentCarFragment : Fragment(R.layout.fragment_current_car) {

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parkedButton: Button = view.findViewById(R.id.btn_parked)

        val makeView: TextView = view.findViewById(R.id.current_Make)
        val modelView: TextView = view.findViewById(R.id.current_Model)
        val plateView: TextView = view.findViewById(R.id.current_Plate)
        val make = arguments?.getString("make")
        val model = arguments?.getString("model")
        val plate = arguments?.getString("plate")
        val id = arguments?.getInt("id")

        makeView.text = make
        modelView.text = model
        plateView.text = plate

        parkedButton.setOnClickListener() {
            thread {
                var db: AppDatabase = AppDatabase.getInstance(view.context)
                if (id != null) {
                    //Set car to not moving
                    db.carDao().carMoving(id, 0)

                    Shared.setLocation(id, view.context)
                }

                //Change fragment
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_fragment, AddFragment())
                    commit()
                }
            }
        }
    }

}
