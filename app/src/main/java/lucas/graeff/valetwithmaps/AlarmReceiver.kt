package lucas.graeff.valetwithmaps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import lucas.graeff.valetwithmaps.database.AppDatabase
import kotlin.concurrent.thread

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //Find database
        var db: AppDatabase = AppDatabase.getInstance(context)

        thread {
            db.carDao().resetLocation(intent.extras?.get("id").toString().toInt())
        }

    }
}
