package id.go.bps.sulbar.simple2.`object`

import android.R.attr.name
import androidx.lifecycle.LiveData
import androidx.room.*


@Entity
data class Prov(
    @PrimaryKey var id: Int,
    var provinsi: String?
){
    override fun toString(): String {
        return provinsi!!
    }
}

@Dao
interface ProvDao {
    @Insert
    fun insertAll(items: List<Prov>)

    @Delete
    fun delete(items: Prov)

    @Query("SELECT * FROM Prov")
    fun getAll(): List<Prov>

    @Query("DELETE FROM Prov")
    fun nukeTable();

    @Query("SELECT * FROM Prov")
    fun getAllLive(): LiveData<List<Prov>>
}


