package id.go.bps.sulbar.simple2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.PrimaryKey
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import id.go.bps.sulbar.simple2.`object`.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class list_dokumen_repo (val db: AppDatabase, val context: Context){

    var list_dokumen_offline: LiveData<List<Dokumen>> = db.dokumenDao().getAllLive()
    var list_dokumen_online: MutableLiveData<ArrayList<Dokumen>> = MutableLiveData<ArrayList<Dokumen>>(ArrayList<Dokumen>())
    val url_list_dokumen = URL.url_bas + "dokumen/list"


    suspend fun fetch_list_dokumen(user: User?, filter: filter, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            if(filter.prov == 0){
                params.put("prov", "00")
            }else{
                params.put("prov", filter.prov)
            }
            if(filter.kab == 0){
                params.put("kab", "0000")
            }else{
                params.put("kab", filter.kab)
            }
            if(filter.kantorUnit == 0){
                params.put("kantor_unit", "000000")
            }else{
                params.put("kantor_unit", filter.kantorUnit)
            }
            if(filter.pelabuhan == 0){
                params.put("pelabuhan_id","00000000")
            }else{
                params.put("pelabuhan_id", filter.pelabuhan)
            }
            params.put("jenis_pelayaran", filter.jenis_pelayaran)
            params.put("bulan", filter.bulan)
            params.put("tahun", filter.tahun)
            val stringRequest = object: JsonObjectRequest(url_list_dokumen, params, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user?.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }
    suspend fun berhasil_fetch_list_laporan(array: JSONArray){
        withContext(Dispatchers.IO) {
            val dokumens = ArrayList<Dokumen>();
            for (num in 0..(array.length()-1)){
                val item = array.getJSONObject(num)
                val dokumen = Dokumen(UUID.randomUUID().toString()
                        , item.getInt("id")
                        , item.optInt("prov")
                        , item.optInt("kab")
                        , item.optInt("kantor_unit_id")
                        , item.optInt("pelabuhan_id")
                        , item.optString("nama_kapal_1")
                        , item.optString("nama_kapal")
                        , item.optString("nama_agen_kapal")
                        , item.optString("bendera")
                        , item.optString("pemilik_agen")
                        , item.optString("berangkat_tgl")
                        , item.optString("berangkat_jam")
                        , item.getInt("status")
                        , item.getInt("approval")
                        , null
                        , System.currentTimeMillis()
                        )
                dokumens.add(dokumen);
            }
            list_dokumen_online.postValue(dokumens)
        }
    }
}