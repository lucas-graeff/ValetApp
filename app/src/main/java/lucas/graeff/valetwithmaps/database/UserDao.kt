package lucas.graeff.valetwithmaps.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getMyCars(): List<User>
}
