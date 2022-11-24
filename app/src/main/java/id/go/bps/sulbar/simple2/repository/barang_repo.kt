package id.go.bps.sulbar.simple2.repository
import android.content.Context
import androidx.lifecycle.LiveData
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

class barang_repo (val db: AppDatabase, val context: Context){

    val url_fetch_barang = URL.url_bas + "barang/load"
    val url_add_barang = URL.url_bas + "barang/add"
    var barang_live: LiveData<List<Barang>> = db.barangDao().getAllLive()


    suspend fun fetch_barang(user: User?, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            val stringRequest = object: JsonObjectRequest(url_fetch_barang, params, success, error) {
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

    suspend fun add_barang(user: User?, barang: Barang, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val stringRequest = object: JsonObjectRequest(url_add_barang, barang.getJSONObject(), success, error) {
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

    suspend fun berhasil_fetch_barang (array: JSONArray){

            val barangss = ArrayList<Barang>();
            for (num in 0..(array.length()-1)){
                val item = array.getJSONObject(num)
                val barang = Barang(item.getInt("id")
                        , item.getString("barang")
                        , item.getString("satuan")
                        , item.getString("konversi"))
                barangss.add(barang)
            }
            db.barangDao().nukeTable()
            db.barangDao().insertAll(barangss)

    }

}