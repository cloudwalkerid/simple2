package id.go.bps.sulbar.simple2.repository
import MySingleton
import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import id.go.bps.sulbar.simple2.`object`.AppDatabase
import id.go.bps.sulbar.simple2.`object`.Dokumen
import id.go.bps.sulbar.simple2.`object`.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*


class dokumen_repo (val db: AppDatabase, val context: Context){

    val url_fetch_dokumen = URL.url_bas + "dokumen/load"
    val url_add_dokumen = URL.url_bas + "dokumen/add"
    val url_edit_dokumen = URL.url_bas + "dokumen/edit"
    val url_hapus_dokumen = URL.url_bas + "dokumen/hapus"
    val url_approve_dokumen = URL.url_bas + "dokumen/approve"

    suspend fun fetch_dokumen_online(user: User, id: Int, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            params.put("key", id);
            val stringRequest = object: JsonObjectRequest(url_fetch_dokumen, params, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }

    suspend fun kirim_add_dokumen(user: User, jsonData: JSONObject, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
//            Log.d("Add_Dokumen", "Bearer "+user.token)
//            Log.d("Add_Dokumen", jsonData.toString())
            val stringRequest = object: JsonObjectRequest(url_add_dokumen, jsonData, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }

    suspend fun kirim_edit_dokumen(user: User, jsonData: JSONObject, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
//            Log.d("Edi_Dokumen", jsonData.toString())
            val stringRequest = object: JsonObjectRequest(url_edit_dokumen, jsonData, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }

    suspend fun kirim_delete_dokumen(user: User, dokumen: Dokumen, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            params.put("key", dokumen.id)
            val stringRequest = object: JsonObjectRequest(url_hapus_dokumen, params, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }

    suspend fun kirim_approve_dokumen(user: User, dokumen: Dokumen, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            params.put("key", dokumen.id)
            val stringRequest = object: JsonObjectRequest(url_approve_dokumen, params, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }
    suspend fun delete_dokumen(dokumen: Dokumen){
        withContext(Dispatchers.IO) {
            db.dokumenDao().delete(dokumen)
        }
    }

    fun read_dokumen_storage(uuid: String): JSONObject{
        val file = File(context.filesDir, uuid)
        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)
        val stringBuilder = StringBuilder()
        var line: String? = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
        bufferedReader.close() // This responce will have Json Format String

        val responce = stringBuilder.toString()
        return JSONObject(responce)
    }
    suspend fun write_dokumen_storage(dokumen: Dokumen, data: JSONObject, type: Int){
        withContext(Dispatchers.IO) {
            val userString: String = data.toString() // Define the File Path and its Name
            val file = File(context.filesDir, dokumen.uuid)
            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
            if(type == 0 || type == 3){
                db.dokumenDao().insertOne(dokumen)
            }else{
                db.dokumenDao().update(dokumen)
            }
        }

    }
}