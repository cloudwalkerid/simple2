package id.go.bps.sulbar.simple2.ui.Entri

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import id.go.bps.sulbar.simple2.`object`.AppDatabase
import id.go.bps.sulbar.simple2.repository.barang_repo
import id.go.bps.sulbar.simple2.repository.user_repo
import id.go.bps.sulbar.simple2.repository.wilayah_repo
import kotlinx.coroutines.launch
import org.json.JSONObject

class TambahPerdaganganActivityModel constructor(
application: Application
) : AndroidViewModel (application) {

    val barangRepo: barang_repo =  barang_repo(AppDatabase.invoke(application), getApplication())
    val barangs = barangRepo.barang_live


}