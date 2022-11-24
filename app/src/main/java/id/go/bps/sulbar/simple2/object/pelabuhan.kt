package id.go.bps.sulbar.simple2.`object`

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Pelabuhan (
    var prov_id: Int,
    var prov: String,
    var kab_id: Int,
    var kab: String,
    var kantor_unit_id: Int,
    var kantor_unit: String,
    @PrimaryKey var pelabuhan_id: Int,
    var pelabuhan: String
){
    override fun toString(): String {
        return pelabuhan!!
    }
}

@Dao
interface PelabuhanDao {
    @Insert
    fun insertAll(items: ArrayList<Pelabuhan>)

    @Delete
    fun delete(items: Pelabuhan)

    @Query("SELECT * FROM Pelabuhan")
    fun getAll(): List<Pelabuhan>

    @Query("DELETE FROM Pelabuhan")
    fun nukeTable();

    @Query("SELECT * FROM Pelabuhan")
    fun getAllLive(): LiveData<List<Pelabuhan>>
}