package id.go.bps.sulbar.simple2.repository


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import id.go.bps.sulbar.simple2.`object`.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.reflect.KSuspendFunction1


class user_repo (private val db: AppDatabase, val context: Context){

    val user: LiveData<User> = db.userDao().getActiveUser()

    val url_login = URL.url_bas + "auth/login"
    val url_logout = URL.url_bas + "auth/logout"
    val url_me = URL.url_bas + "auth/me"
    val url_refresh = URL.url_bas + "auth/refresh"

    suspend fun fetch_login(username: String, password: String, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            params.put("username", username)
            params.put("password", password)
            val stringRequest = JsonObjectRequest( url_login, params, success, error)
                // Add the request to the RequestQueue.
            MySingleton.getInstance(context).requestQueue.add(stringRequest)
        }
    }
    suspend fun fetch_me(success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            val stringRequest = object: JsonObjectRequest(url_me, params, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.value!!.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }
    suspend fun fetch_refresh(berhasilRefresh: (JSONObject) -> Unit){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            Log.d("Refresh", "Refresh")
            val stringRequest = object: JsonObjectRequest(url_refresh, params, Response.Listener<JSONObject>{
                Log.d("Refresh", it.toString())
                berhasilRefresh(it)
//                launch {
//                    berhasilRefresh(it)
//                }
            },Response.ErrorListener{
                Log.e("Refresh", it.printStackTrace().toString())
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.value!!.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }
    suspend fun berhasilRefresh(hasil: JSONObject){
        withContext(Dispatchers.IO) {
            val wilayahRepo = wilayah_repo(db)
            db_refreshUser(hasil.getString("access_token"))
            wilayahRepo.insert_prov(hasil.getJSONArray("prov"))
            wilayahRepo.insert_kab(hasil.getJSONArray("kab"))
            wilayahRepo.insert_kantor_unit(hasil.getJSONArray("kantor_unit"))
            wilayahRepo.insert_pelabuhan(hasil.getJSONArray("pelabuhan"))
        }
    }
    suspend fun fetch_logout(success: Response.Listener<JSONObject>, error: Response.ErrorListener){
        withContext(Dispatchers.IO) {
            val params = JSONObject()
            val stringRequest = object: JsonObjectRequest(url_logout, params, success, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer "+user.value!!.token
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest)
        }
    }
    suspend fun db_insertUser(userJson: JSONObject, access_token: String){
        withContext(Dispatchers.IO) {
            val user = User(userJson.getInt("id")
                , userJson.getString("nama")
                , userJson.getString("username")
                , userJson.optInt("prov_id")
                , userJson.optInt("kab_id")
                , userJson.optInt("kantor_unit_id")
                , userJson.optInt("pelabuhan_id")
                , userJson.getInt("leveluser_id")
                , access_token
                , System.currentTimeMillis()
                    , System.currentTimeMillis(), 0)
            db.userDao().nukeTable()
            db.userDao().insertAll(user)
        }
    }
    suspend fun db_refreshUser(access_token: String){
        withContext(Dispatchers.IO) {
            val userItem = db.userDao().getActiveUserNotLive()
            userItem.token = access_token
            userItem.date_refresh = System.currentTimeMillis()
            db.userDao().update(userItem)
        }
    }
    suspend fun change_status_401(status: Int){
        withContext(Dispatchers.IO) {
            val userItem = db.userDao().getActiveUserNotLive()
            userItem.status = status
            userItem.date_refresh = 0L
            db.userDao().update(userItem)
        }
    }
    suspend fun change_status(status: Int){
        withContext(Dispatchers.IO) {
            val userItem = db.userDao().getActiveUserNotLive()
            userItem.status = status
            db.userDao().update(userItem)
        }
    }
    suspend fun db_nuke(){
        withContext(Dispatchers.IO) {
            db.userDao().nukeTable()
            db.provDao().nukeTable()
            db.kabDao().nukeTable()
            db.kantorUnitDao().nukeTable()
            db.pelabuhanDaou().nukeTable()
            db.barangDao().nukeTable()
            db.dokumenDao().nukeTable()
        }
    }
}