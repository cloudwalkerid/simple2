package id.go.bps.sulbar.simple2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.*
import id.go.bps.sulbar.simple2.`object`.*
import id.go.bps.sulbar.simple2.repository.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*


class MainActivityViewModel constructor(
        application: Application
) : AndroidViewModel (application) {

    val bulan: Int = Date().month + 1
    val tahun: Int = Date().year + 1900

    val userRepo: user_repo = user_repo(AppDatabase.invoke(application), application)
    val user: LiveData<User> = userRepo.user

    val wilayahRepo : wilayah_repo = wilayah_repo(AppDatabase.invoke(application))
//    val mainRepo: main_repo = main_repo(MySingleton.getInstance(this.getApplication()).requestQueue)
    val dashboardRepo: dashboard_repo = dashboard_repo(application)
    val listDokumenRepo: list_dokumen_repo = list_dokumen_repo(AppDatabase.invoke(application), application)
    val laporanRepo: laporan_repo = laporan_repo(application)
    val dokumenRepo: dokumen_repo = dokumen_repo(AppDatabase.invoke(application), application)

    var selection_list_dokumen = filter(0, 0, 0, 0, bulan, tahun, 0)
    var selection_laporan = filter(0, 0, 0, 0, bulan, tahun, 0)

    var processLogout: MutableLiveData<Int> = MutableLiveData<Int>(0) // 1: melakukan process 2: success process 3: gagal process

//    var processMain: MutableLiveData<Int> = MutableLiveData<Int>(0) // 1: melakukan process 2: success process 3: gagal process

    var processDashboard: MutableLiveData<Int> = MutableLiveData<Int>(0) // 1: melakukan process 2: success process 3: gagal process
    var processListDoku: MutableLiveData<Int> = MutableLiveData<Int>(0) // 1: melakukan process 2: success process 3: gagal process
    var processLaporan: MutableLiveData<Int> = MutableLiveData<Int>(0) // 1: melakukan process 2: success process 3: gagal process

    var processLogoutPesan: MutableLiveData<String> = MutableLiveData<String>("")
    var processMainPesan: MutableLiveData<String> = MutableLiveData<String>("")
    var processDashboardPesan: MutableLiveData<String> = MutableLiveData<String>("")
    var processListDokuPesan: MutableLiveData<String> = MutableLiveData<String>("")
    var processLaporanPesan: MutableLiveData<String> = MutableLiveData<String>("")

    fun check_auth(user: User): Boolean{
        if(user.isRefreshTTLExpired() || user.isRefreshExpired()){
            viewModelScope.launch {
                userRepo.change_status(1)
            }
            return false
        }else{
            return true
        }
    }

    fun berhasilRefresh(hasil: JSONObject){
        viewModelScope.launch{
            userRepo.berhasilRefresh(hasil)
        }
    }

    fun logout(){
        processLogout.value = 1
        viewModelScope.launch {
            userRepo.fetch_logout(Response.Listener<JSONObject>{
                Log.d("Login", it.toString())
                viewModelScope.launch{
                    userRepo.db_nuke()
                }
                processLogout.value = 2
            },Response.ErrorListener{
                if (it is AuthFailureError){
                    Log.d("Login", it.toString())
                    viewModelScope.launch{
                        userRepo.db_nuke()
                    }
                    processLogout.value = 2
                }else{
                    Log.e("Login", it.printStackTrace().toString())
                    processLogout.value = 3
                    pesanError(processLogoutPesan, it)
                }
            })
        }
    }


    fun fetch_dashboard(){
        Log.d("Main", "fetch_dashboard")
        if(!check_auth(user.value!!)){
            return
        }
        viewModelScope.launch {
            dashboardRepo.fetch_dashboard(user.value, bulan, tahun, Response.Listener<JSONObject>{
                Log.d("Dashboard ", it.toString())
                dashboardRepo.parse_hasil(it)
                processDashboard.value = 2
            },Response.ErrorListener{
                Log.e("Dashboard ", it.printStackTrace().toString())
                pesanError(processDashboardPesan, it)
                processDashboard.value = 3
            })
        }
    }
    fun fetch_dokumen_list(){
        Log.d("Main", "fetch_dokumen_list")
        if(!check_auth(user.value!!)){
            return
        }
        viewModelScope.launch {
            listDokumenRepo.fetch_list_dokumen(user.value, selection_list_dokumen, Response.Listener<JSONObject>{
                Log.d("List Doku ", it.toString())
                viewModelScope.launch {
                    listDokumenRepo.berhasil_fetch_list_laporan(it.getJSONArray("list"))
                    processListDoku.value = 2;
                }
            },Response.ErrorListener{
                Log.e("List Doku ", it.printStackTrace().toString())
                pesanError(processListDokuPesan, it)
                processListDoku.value = 3
            })
        }
    }
    fun fetch_laporan(){
        Log.d("Main", "fetch_laporan")
        if(!check_auth(user.value!!)){
            return
        }
        viewModelScope.launch {
            laporanRepo.fetch_laporan(user.value, selection_laporan, Response.Listener<JSONObject>{
                Log.d("Laporan ", it.toString())
                laporanRepo.parse_hasil(it)
                processLaporan.value = 2
            },Response.ErrorListener{
                Log.e("Laporan ", it.printStackTrace().toString())
                pesanError(processLaporanPesan, it)
                processLaporan.value = 3
            })
        }
    }

    fun refresh(){
        viewModelScope.launch {
            userRepo.fetch_refresh(::berhasilRefresh)
        }
    }

    fun pesanError(processLaporanPesan: MutableLiveData<String>, it: VolleyError?) {
        if(it == null){
            processLaporanPesan.value = "Terjadi kesalahan\ncoba beberapa menit lagi"
        }else if (it is TimeoutError || it is NoConnectionError) {
            //This indicates that the reuest has either time out or there is no connection
            processLaporanPesan.value = "Tidak ada koneksi internet!"
        } else if (it is AuthFailureError) {
            // Error indicating that there was an Authentication Failure while performing the request
            processLaporanPesan.value = "Tidak mempunyai akses untuk proses ini!"
            viewModelScope.launch {
                userRepo.change_status_401(1)
            }
        } else if (it is ServerError) {
            //Indicates that the server responded with a error response
            processLaporanPesan.value = "Terjadi kesalahan di server!"
        } else if (it is NetworkError) {
            //Indicates that there was network error while performing the request
            processLaporanPesan.value = "Tidak ada koneksi internet!"
        } else if (it is ParseError) {
            // Indicates that the server response could not be parsed
            processLaporanPesan.value = "Terjadi kesalahan !"
        }
    }
    fun deleteDokumenoffline(dokumen: Dokumen, berhasil:(Dokumen)->Unit, gagal:(Dokumen)->Unit){
        viewModelScope.launch {
            try{
                dokumenRepo.delete_dokumen(dokumen)
                berhasil(dokumen)
            }catch (e: Exception){
                gagal(dokumen)
                Log.d("Delete", ""+e.printStackTrace())
            }

        }
    }
    fun deleteDokumenofflineHelp(dokumen: Dokumen){
        viewModelScope.launch {
            dokumenRepo.delete_dokumen(dokumen)
        }
    }
    fun kirimDokumenoffline(dokumen: Dokumen, berhasil:(Dokumen)->Unit, gagal:(Dokumen)->Unit){
        if(!check_auth(user.value!!)){
            return
        }
        viewModelScope.launch {
            if(dokumen.id == -1){
                dokumenRepo.kirim_add_dokumen(user.value !!,dokumenRepo.read_dokumen_storage(dokumen.uuid), Response.Listener<JSONObject>{
                    deleteDokumenofflineHelp(dokumen)
                    berhasil(dokumen)
                },Response.ErrorListener{
                    if(it != null){
                        Log.d("Kirim Offline", ""+it)
                    }
                    Log.d("Kirim Offline", ""+it.printStackTrace())
                    gagal(dokumen)
                })
            }else{
                dokumenRepo.kirim_edit_dokumen(user.value !!,dokumenRepo.read_dokumen_storage(dokumen.uuid), Response.Listener<JSONObject>{
                    deleteDokumenofflineHelp(dokumen)
                    berhasil(dokumen)
                },Response.ErrorListener{
                    if(it.message != null){
                        Log.d("Kirim Offline", it.message!!)
                    }
                    Log.d("Kirim Offline", ""+it.printStackTrace())
                    gagal(dokumen)
                })
            }

        }
    }
    fun deleteDokumenOnline(dokumen: Dokumen, berhasil:(Dokumen)->Unit, gagal:(Dokumen)->Unit){
        viewModelScope.launch {
            dokumenRepo.kirim_delete_dokumen(user.value!!, dokumen, Response.Listener<JSONObject>{
                Log.d("Delete Online ", it.toString())
                berhasil(dokumen)
            },Response.ErrorListener{
                if(it.message != null){
                    Log.d("Delete Online", it.message!!)
                }
                Log.d("Delete Online", it.printStackTrace().toString())
                gagal(dokumen)
            })
        }
    }

    fun approveDokumen(dokumen: Dokumen, berhasil:(Dokumen)->Unit, gagal:(Dokumen)->Unit){
        if(!check_auth(user.value!!)){
            return
        }
        viewModelScope.launch {
            dokumenRepo.kirim_approve_dokumen(user.value !!,dokumen, Response.Listener<JSONObject>{
                Log.d("Approve ", it.toString())
                berhasil(dokumen)
            },Response.ErrorListener{
                Log.d("Approve ", it.toString())
                gagal(dokumen)
            })
        }
    }
}
