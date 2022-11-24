package id.go.bps.sulbar.simple2.`object`

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Dokumen (
    @PrimaryKey var uuid: String,
    var id: Int?,
    var prov_id: Int?,
    var kab_id: Int?,
    var kantorUnit_id: Int?,
    var pelabuhan_id: Int?,
    var nama_kapal_1: String?,
    var nama_kapal: String?,
    var nama_agen_kapal: String?,
    var bendera: String?,
    var pemilik_agen: String?,
    var berangkat_tgl: String?,
    var berangkat_jam: String?,
    var status: Int,
    var approval: Int,
    var jml_error: Int?,
    var lastEdited: Long
){
    fun getWilayahKabProv(provs: List<Prov>, kabs: List<Kab>): String{
        var provString: String? = provs.find {it.id == prov_id }?.provinsi
        var kabString: String? = kabs.find {it.prov_id == prov_id && it.kab_id == kab_id }?.kab
        if( provString == null || kabString == null){
            return "Data Wilayah Belum Lengkap"
        }else{
            return provString+" "+kabString
        }
    }
    fun getWilayahPelabuhan(pelabuhans: List<Pelabuhan>): String{
        var pelabuhanString: String? = pelabuhans.find {it.prov_id == prov_id && it.kab_id == kab_id
                && it.kantor_unit_id == kantorUnit_id && it.pelabuhan_id == pelabuhan_id}?.pelabuhan
        if( pelabuhanString == null){
            return "Data Pelabuhan Belum Lengkap"
        }else{
            return pelabuhanString
        }
    }
    fun getBerangkat(): String{
        if(berangkat_tgl == null || berangkat_tgl == "" || berangkat_jam == null || berangkat_jam == ""){
            return "Data Keberangkatan Belum Lengkap"
        }else{
            val splitDate =  berangkat_tgl!!.split("-")
            val splitJam = berangkat_jam!!.split(":")
            val bulan: Array<String> = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember");
            return "Berangkat, "+splitDate[2]+" "+bulan[splitDate[1].toInt()-1]+" "+splitDate[0]+" "+splitJam[0]+":"+splitJam[1]
        }
    }
}

@Dao
interface DokumenDao {
    @Insert
    fun insertAll(items: ArrayList<Dokumen>)

    @Insert
    fun insertOne(items: Dokumen)

    @Update
    fun update(vararg items: Dokumen)

    @Delete
    fun delete(items: Dokumen)

    @Query("SELECT * FROM Dokumen")
    fun getAll(): List<Dokumen>

    @Query("DELETE FROM Dokumen")
    fun nukeTable();

    @Query("SELECT * FROM Dokumen")
    fun getAllLive(): LiveData<List<Dokumen>>
}