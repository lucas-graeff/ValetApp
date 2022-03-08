package lucas.graeff.valetwithmaps.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "make") val make: String,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "plate") val plate: String,
    @ColumnInfo(name = "called") val called: Int,
    @ColumnInfo(name = "moving") var moving: Int,
    @ColumnInfo(name = "in_possession") val in_possession: Int,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "latitude") val latitude: Double?
)