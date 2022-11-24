package id.go.bps.sulbar.simple2.repository

import androidx.lifecycle.LiveData
import id.go.bps.sulbar.simple2.`object`.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class wilayah_repo (val db: AppDatabase) {

    val provLive: LiveData<List<Prov>> = db.provDao().getAllLive()
    val kabLive: LiveData<List<Kab>> = db.kabDao().getAllLive()
    val kantorUnitLive: LiveData<List<Kantor_Unit>> = db.kantorUnitDao().getAllLive()
    val pelabuhanLive: LiveData<List<Pelabuhan>> = db.pelabuhanDaou().getAllLive()

    suspend fun insert_prov(array: JSONArray){
        withContext(Dispatchers.IO) {
            val provs = ArrayList<Prov>();
            for (num in 0..(array.length()-1)){
                val item = array.getJSONObject(num)
                val prov = Prov(item.getInt("id")
                        , item.getString("provinsi"))
                provs.add(prov);
            }
            db.provDao().nukeTable()
            db.provDao().insertAll(provs)
        }
    }
    suspend fun insert_kab(array: JSONArray){
        withContext(Dispatchers.IO) {
            val kabs = ArrayList<Kab>();
            for (num in 0..(array.length()-1)){
                val item = array.getJSONObject(num)
                val kab = Kab(item.getInt("prov_id")
                        , item.getString("prov")
                        , item.getInt("kab_id")
                        , item.getString("kab"))
                kabs.add(kab);
            }
            db.kabDao().nukeTable()
            db.kabDao().insertAll(kabs)
        }
    }
    suspend fun insert_kantor_unit(array: JSONArray){
        withContext(Dispatchers.IO) {
            val kantor_units = ArrayList<Kantor_Unit>();
            for (num in 0..(array.length()-1)){
                val item = array.getJSONObject(num)
                val kantor_unit= Kantor_Unit(item.getInt("prov_id")
                        , item.getString("prov")
                        , item.getInt("kab_id")
                        , item.getString("kab")
                        , item.getInt("kantor_unit_id")
                        , item.getString("kantor_unit"))
                kantor_units.add(kantor_unit);
            }
            db.kantorUnitDao().nukeTable()
            db.kantorUnitDao().insertAll(kantor_units)
        }
    }
    suspend fun insert_pelabuhan(array: JSONArray){
        withContext(Dispatchers.IO) {
            val pelabuhasn = ArrayList<Pelabuhan>();
            for (num in 0..(array.length()-1)){
                val item = array.getJSONObject(num)
                val pelabuhan = Pelabuhan(item.getInt("prov_id")
                        , item.getString("prov")
                        , item.getInt("kab_id")
                        , item.getString("kab")
                        , item.getInt("kantor_unit_id")
                        , item.getString("kantor_unit")
                        , item.getInt("pelabuhan_id")
                        , item.getString("pelabuhan"))
                pelabuhasn.add(pelabuhan);
            }
            db.pelabuhanDaou().nukeTable()
            db.pelabuhanDaou().insertAll(pelabuhasn)
        }
    }
}