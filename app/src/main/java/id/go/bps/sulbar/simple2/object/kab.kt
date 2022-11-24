package id.go.bps.sulbar.simple2.`object`

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Kab (
    var prov_id: Int,
    var prov: String,
    @PrimaryKey var kab_id: Int,
    var kab: String
){
    override fun toString(): String {
        return kab!!
    }
}


@Dao
interface KabDao {
    @Insert
    fun insertAll(items: ArrayList<Kab>)

    @Delete
    fun delete(items: Kab)

    @Query("SELECT * FROM Kab")
    fun getAll(): List<Kab>

    @Query("DELETE FROM Kab")
    fun nukeTable();

    @Query("SELECT * FROM Kab")
    fun getAllLive(): LiveData<List<Kab>>
}