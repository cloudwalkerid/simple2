package id.go.bps.sulbar.simple2.repository
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import id.go.bps.sulbar.simple2.`object`.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class laporan_repo (val context: Context){

    val url_fetch_laporan = URL.url_bas + "laporan"
    var waktu : MutableLiveData<String> = MutableLiveData<String>("")
    var unit : MutableLiveData<Int> = MutableLiveData<Int>(0)
    var penumpang_datang : MutableLiveData<String> = MutableLiveData<String>("")
    var penumpang_berangkat : MutableLiveData<String> = MutableLiveData<String>("")
    var ton_bongkar : MutableLiveData<String> = MutableLiveData<String>("")
    var ton_muat : MutableLiveData<String> = MutableLiveData<String>("")
    
    suspend fun fetch_laporan(user: User?, filter: filter, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
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
            params.put("bulan", filter.bulan)
            params.put("tahun", filter.tahun)
            val stringRequest = object: JsonObjectRequest(url_fetch_laporan, params, success, error) {
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
    fun parse_hasil(objectHasil: JSONObject){
        waktu.value = objectHasil.getString("waktu")
        unit.value = objectHasil.getInt("unit")
        penumpang_datang.value = objectHasil.getString("penumpang_datang")
        penumpang_berangkat.value = objectHasil.getString("penumpang_berangkat")
        ton_bongkar.value = objectHasil.getString("ton_bongkar")
        ton_muat.value = objectHasil.getString("ton_muat")
    }
}