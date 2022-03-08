package lucas.graeff.valetwithmaps.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CarDao {
    @Query("SELECT * FROM Car")
    fun getMyCars(): List<Car>

    @Query("SELECT * FROM Car WHERE called == 1")
    fun getCalledCars(): MutableList<Car>

    @Query("SELECT * FROM Car WHERE latitude is not null")
    fun getParkedCars(): List<Car>

    @Query("SELECT * FROM Car WHERE id == :id")
    fun getCarById(id: Int): Car

    @Query("SELECT * FROM Car WHERE plate == :plate")
    fun getCarByPlate(plate: String): Car

    @Query("UPDATE Car SET called = :called WHERE id = :id")
    fun requestCar(id: Int, called: Int)

    @Query("UPDATE Car SET in_possession = :in_possession WHERE id = :id")
    fun carPossessed(in_possession: Int, id: Int)

    @Query("UPDATE Car SET moving = :moving WHERE id = :id")
    fun carMoving(id: Int, moving: Int)

    @Query("UPDATE Car SET longitude = :longitude, latitude = :latitude WHERE id= :id")
    fun carLocation(longitude: Double, latitude: Double, id: Int)

    @Query("UPDATE Car SET longitude = NULL, latitude = NULL WHERE id= :id")
    fun resetLocation(id: Int)

    @Insert
    fun addCar(car: Car)

    @Update
    fun updateCar(car: Car)

}