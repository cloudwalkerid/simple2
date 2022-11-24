package id.go.bps.sulbar.simple2

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import id.go.bps.sulbar.simple2.`object`.AppDatabase
import id.go.bps.sulbar.simple2.repository.barang_repo
import id.go.bps.sulbar.simple2.repository.user_repo
import id.go.bps.sulbar.simple2.repository.wilayah_repo
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginActivityViewModel constructor(
    application: Application
    ) : AndroidViewModel (application) {

    val userRepo: user_repo = user_repo(AppDatabase.invoke(application), getApplication() )
    val wilayahRepo : wilayah_repo = wilayah_repo(AppDatabase.invoke(application))
    val barangRepo: barang_repo =  barang_repo(AppDatabase.invoke(application), getApplication())
    val user = userRepo.user
    var processLogin: MutableLiveData<Int> = MutableLiveData<Int>() // 1: melakukan login 2: success login 3: gagal login


    fun login(username: String, password: String){
        processLogin.value = 1
        viewModelScope.launch {
            userRepo.fetch_login(username, password, Response.Listener<JSONObject>{
                Log.d("Login", it.toString())
                berhasilLogin(it)
            },Response.ErrorListener{
                Log.e("Login", it.printStackTrace().toString())
                processLogin.value = 3
            })
        }
    }
    fun berhasilLogin(hasil: JSONObject){
        processLogin.value = 2
        viewModelScope.launch {
            if(user.value != null){
                if(user.value!!.id != hasil.getJSONObject("user").getInt("id")){
                    userRepo.db_nuke()
                }
            }else{
                userRepo.db_nuke()
            }
            userRepo.db_insertUser(hasil.getJSONObject("user"), hasil.getString("access_token"))
            wilayahRepo.insert_prov(hasil.getJSONArray("prov"))
            wilayahRepo.insert_kab(hasil.getJSONArray("kab"))
            wilayahRepo.insert_kantor_unit(hasil.getJSONArray("kantor_unit"))
            wilayahRepo.insert_pelabuhan(hasil.getJSONArray("pelabuhan"))
            barangRepo.berhasil_fetch_barang(hasil.getJSONArray("master_barang"))
        }
    }

}
