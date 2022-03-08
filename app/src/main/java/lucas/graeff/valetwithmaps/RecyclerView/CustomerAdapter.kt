package lucas.graeff.valetwithmaps.RecyclerView

import android.content.Context
import android.util.Log
import android.util.Log.DEBUG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lucas.graeff.valetwithmaps.R
import lucas.graeff.valetwithmaps.database.AppDatabase
import lucas.graeff.valetwithmaps.database.Car
import kotlin.concurrent.thread


class CustomerAdapter(private val cars: List<Car>, private val context: Context) :
    RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val makeView: TextView = view.findViewById(R.id.text_Make)
        val modelView: TextView = view.findViewById(R.id.text_Model)
        val plateView: TextView = view.findViewById(R.id.text_Plate)
        val callBtn: Button = view.findViewById(R.id.btn_request)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.car_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val db: AppDatabase = AppDatabase.getInstance(context)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.makeView.text = "${cars[position].year} ${cars[position].make}"
        viewHolder.modelView.text = cars[position].model
        viewHolder.plateView.text = cars[position].plate

        //Set visibility of button
        if(cars[position].in_possession == 0) {
            viewHolder.callBtn.visibility = View.GONE
        }

        //Request Car
        viewHolder.callBtn.setOnClickListener(){
            //Disable button
            viewHolder.callBtn.isEnabled = false
            viewHolder.callBtn.text = "Requested"
            //Db call
            val userDao = db.carDao()
            thread {
                userDao.requestCar(cars[position].id, 1)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = cars.size

}
