//package id.go.bps.sulbar.simple2.repository
//import com.android.volley.RequestQueue
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//import id.go.bps.sulbar.simple2.`object`.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import org.json.JSONObject
//
//class main_repo (private val queue: RequestQueue){
//
//    val url_main = URL.url_bas + "main"
//
//    suspend fun fetch_main(user: User?, success: Response.Listener<JSONObject>, error: Response.ErrorListener){
//        withContext(Dispatchers.IO) {
//            val params = JSONObject()
//            val stringRequest = object: JsonObjectRequest(url_main, params, success, error) {
//                override fun getHeaders(): MutableMap<String, String> {
//                    val headers = HashMap<String, String>()
//                    headers["Authorization"] = "Bearer "+user?.token
//                    headers["Accept"] = "application/json"
//                    headers["Content-Type"] = "application/json"
//                    return headers
//                }
//            }
//            // Add the request to the RequestQueue.
//            queue.add(stringRequest)
//        }
//    }
//}