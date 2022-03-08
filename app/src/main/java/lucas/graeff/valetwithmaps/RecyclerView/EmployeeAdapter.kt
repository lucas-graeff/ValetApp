package lucas.graeff.valetwithmaps.RecyclerView

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lucas.graeff.valetwithmaps.AlarmReceiver
import lucas.graeff.valetwithmaps.R
import lucas.graeff.valetwithmaps.database.AppDatabase
import lucas.graeff.valetwithmaps.database.Car
import lucas.graeff.valetwithmaps.database.Shared
import kotlin.concurrent.thread


class EmployeeAdapter(private val dataSet: MutableList<Car>, private val context: Context) :
    RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val makeView: TextView = view.findViewById(R.id.text_Make)
        val modelView: TextView = view.findViewById(R.id.text_Model)
        val plateView: TextView = view.findViewById(R.id.text_Plate)
        val requestedBtn: Button = view.findViewById(R.id.btn_parked)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.requested_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.makeView.text = "${dataSet[position].year} ${dataSet[position].make}"
        viewHolder.modelView.text = dataSet[position].model
        viewHolder.plateView.text = dataSet[position].plate

        //Request Car
        viewHolder.requestedBtn.setOnClickListener() {
            val db: AppDatabase = AppDatabase.getInstance(context)
            val id = dataSet[position].id

            if (dataSet[position].moving == 0) {
                //Set to moving
                dataSet[position].moving = 1

                viewHolder.requestedBtn.text = "Dropped off"
            } else {
                dataSet[position].moving = 0

                //Set LatLong
                Shared.setLocation(id, context)

                //Update db with requested value and possession value
                thread {
                    db.carDao().requestCar(id, 0)
                    db.carDao().carPossessed(0, id)
                }

                //Set alarm to unpark and remove location in 15 mins
                var alarmMgr: AlarmManager? = null
                lateinit var alarmIntent: PendingIntent
                val intent = Intent(context, AlarmReceiver::class.java)
                intent.putExtra("id", id)
                alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmIntent = intent.let { intent ->
                    PendingIntent.getBroadcast(context, 0, intent, 0)
                }

                alarmMgr.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 600 * 1000,
                    alarmIntent
                )

                //Hide this row
                viewHolder.itemView.visibility = View.GONE
                val params: ViewGroup.LayoutParams = viewHolder.itemView.layoutParams
                params.height = 0;
                viewHolder.itemView.layoutParams = params

            }
            //Update db with moving value
            thread {
                db.carDao().carMoving(dataSet[position].moving, id)
            }

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

