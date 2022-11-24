package id.go.bps.sulbar.simple2.repository
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import id.go.bps.sulbar.simple2.`object`.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class dashboard_repo (val context: Context){

    val url_dashboard = URL.url_bas + "dashboard"
    var waktu : MutableLiveData<String> = MutableLiveData<String>("")
    var laporan_total : MutableLiveData<Int> = MutableLiveData<Int>(0)
    var laporan_sudah_approve : MutableLiveData<Int> = MutableLiveData<Int>(0)
    var laporan_belum_approve : MutableLiveData<Int> = MutableLiveData<Int>(0)
    var laporan_belum_selesai : MutableLiveData<Int> = MutableLiveData<Int>(0)

    suspend fun fetch_dashboard(user: User?, bulan: Int, tahun: Int, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            params.put("bulan", bulan)
            params.put("tahun", tahun)
            val stringRequest = object: JsonObjectRequest(url_dashboard, params, success, error) {
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

    fun parse_hasil(hasil: JSONObject){
        waktu.value = hasil.getString("waktu");
        laporan_total.value = hasil.getInt("laporan_total")
        laporan_sudah_approve.value = hasil.getInt("laporan_sudah_approve")
        laporan_belum_approve.value = hasil.getInt("laporan_belum_approve")
        laporan_belum_selesai.value = hasil.getInt("laporan_belum_selesai")
    }
}