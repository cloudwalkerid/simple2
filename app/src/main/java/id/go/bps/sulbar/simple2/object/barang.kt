package id.go.bps.sulbar.simple2.`object`

import androidx.lifecycle.LiveData
import androidx.room.*
import org.json.JSONObject

@Entity
data class Barang (
        @PrimaryKey var id: Int,
        var barang: String,
        var satuan: String,
        var konversi: String){

    fun getSatuanList(): List<String>{
        return this.satuan.split(",")
    }
    fun getKonversiList(): List<Double>{
        var listKonversi: List<Double> = emptyList()
        this.konversi.split(",").forEach {
            listKonversi = listKonversi.plus(it.toDouble());
        }
        return  listKonversi
    }
    fun getJSONObject(): JSONObject{
        val returnValue = JSONObject()
        returnValue.put("id",0)
        returnValue.put("barang",barang)
        returnValue.put("satuan",getSatuanList().joinToString { "," })
        returnValue.put("konversi", getKonversiList().joinToString { "," })
        return returnValue
    }

    override fun toString(): String {
        return barang
    }
}

@Dao
interface BarangDao {
    @Insert
    fun insertAll(items: ArrayList<Barang>)

    @Delete
    fun delete(items: Barang)

    @Query("SELECT * FROM Barang")
    fun getAll(): List<Barang>

    @Query("DELETE FROM Barang")
    fun nukeTable();

    @Query("SELECT * FROM Barang")
    fun getAllLive(): LiveData<List<Barang>>
}