package id.go.bps.sulbar.simple2.`object`

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Kantor_Unit (
    var prov_id: Int,
    var prov: String,
    var kab_id: Int,
    var kab: String,
    @PrimaryKey var kantor_unit_id: Int,
    var kantor_unit: String
){
    override fun toString(): String {
        return kantor_unit!!
    }
}

@Dao
interface Kantor_UnitDao {
    @Insert
    fun insertAll(items: ArrayList<Kantor_Unit>)

    @Delete
    fun delete(items: Kantor_Unit)

    @Query("SELECT * FROM Kantor_Unit")
    fun getAll(): List<Kantor_Unit>

    @Query("DELETE FROM Kantor_Unit")
    fun nukeTable();

    @Query("SELECT * FROM Kantor_Unit")
    fun getAllLive(): LiveData<List<Kantor_Unit>>
}