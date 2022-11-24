package id.go.bps.sulbar.simple2

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import id.go.bps.sulbar.simple2.`object`.AppDatabase
import id.go.bps.sulbar.simple2.`object`.Dokumen
import id.go.bps.sulbar.simple2.`object`.User
import id.go.bps.sulbar.simple2.`object`.perdagangan
import id.go.bps.sulbar.simple2.repository.barang_repo
import id.go.bps.sulbar.simple2.repository.dokumen_repo
import id.go.bps.sulbar.simple2.repository.user_repo
import id.go.bps.sulbar.simple2.repository.wilayah_repo
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class EntriActivityViewModel constructor(
        application: Application
) : AndroidViewModel (application) {

    val userRepo: user_repo = user_repo(AppDatabase.invoke(application), application)
    val wilayahRepo : wilayah_repo = wilayah_repo(AppDatabase.invoke(application))
    val dokumenRepo: dokumen_repo = dokumen_repo(AppDatabase.invoke(application), application)
    val barangRepo: barang_repo = barang_repo(AppDatabase.invoke(application), application)

    val user = userRepo.user
    var type: Int? = null // 0: lihat 1: Tambah 2: Edit offline 3: Edit online
    var id: Int? = null
    var uuid: String? = null

    //error
//    var jml_eror: MutableLiveData<Int> = MutableLiveData<Int>(0)
//    var jml_error_blok_1: Int = 0
//    var jml_error_blok_2: Int = 0
//    var jml_error_blok_3_1: Int = 0
//    var jml_error_blok_3_2: Int = 0
//    var jml_error_blok_4_1: Int = 0
//    var jml_error_blok_4_2: Int = 0
//    var jml_error_blok_5: Int = 0

//    fun renew_error(){
//        jml_eror.value = jml_error_blok_1
//            + jml_error_blok_2
//            + jml_error_blok_3_1
//            + jml_error_blok_3_2
//            + jml_error_blok_4_1
//            + jml_error_blok_4_2
//            + jml_error_blok_5
//    }

    //blok 1
    var prov: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var kab: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var kantorUnit: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var pelabuhan: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var jenis_pelayaran: MutableLiveData<Int> = MutableLiveData<Int>(0)

    //blok 2
    var nama_kapal: MutableLiveData<String> = MutableLiveData<String>("")
    var nama_kapal_1: MutableLiveData<String> = MutableLiveData<String>("")
    var bendera: MutableLiveData<String> = MutableLiveData<String>("")
    var pemilik: MutableLiveData<String> = MutableLiveData<String>("")
    var nama_agen_kapal: MutableLiveData<String> = MutableLiveData<String>("")

    var panjang_kapal: MutableLiveData<Double> = MutableLiveData<Double>(0.0)
    var panjang_grt: MutableLiveData<Double> = MutableLiveData<Double>(0.0)
    var volume_nrt: MutableLiveData<Double> = MutableLiveData<Double>(0.0)
    var panjang_dwt: MutableLiveData<Double> = MutableLiveData<Double>(0.0)

    var tiba_tgl: MutableLiveData<String> = MutableLiveData<String>("")
    var tiba_jam: MutableLiveData<String> = MutableLiveData<String>("")
    var tiba_pelab_asal: MutableLiveData<String> = MutableLiveData<String>("")

    var tambat_tgl: MutableLiveData<String> = MutableLiveData<String>("")
    var tambat_jam: MutableLiveData<String> = MutableLiveData<String>("")
    var tambat_jenis: MutableLiveData<String> = MutableLiveData<String>("")

    var berangkat_tgl: MutableLiveData<String> = MutableLiveData<String>("")
    var berangkat_jam: MutableLiveData<String> = MutableLiveData<String>("")
    var berangkat_pelab_tujuan: MutableLiveData<String> = MutableLiveData<String>("")

    var penumpang_naik: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var penumpang_turun: MutableLiveData<Int> = MutableLiveData<Int>(0)

    //blok 3_1 3_2 4_1 4_2
    var pdn_bongkar: MutableLiveData<ArrayList<perdagangan>> = MutableLiveData<ArrayList<perdagangan>>(ArrayList())
    var pdn_muat: MutableLiveData<ArrayList<perdagangan>> = MutableLiveData<ArrayList<perdagangan>>(ArrayList())
    var pln_bongkar: MutableLiveData<ArrayList<perdagangan>> = MutableLiveData<ArrayList<perdagangan>>(ArrayList())
    var pln_muat: MutableLiveData<ArrayList<perdagangan>> = MutableLiveData<ArrayList<perdagangan>>(ArrayList())

    //blok 5
    var keterangan: MutableLiveData<String> = MutableLiveData<String>("")
    var status: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var approval: MutableLiveData<Int> = MutableLiveData<Int>(0)
    //all
    var error: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var process_load: MutableLiveData<Int> = MutableLiveData(0) // 0 tidak ada 1 start 2 berhasil dapat 3 gagal dapat 4 start load 5 berhasil load

    fun fetch_dokumen_online(){
        viewModelScope.launch {
            process_load.value = 1
            dokumenRepo.fetch_dokumen_online(user.value!!, id!! , Response.Listener<JSONObject>{
                Log.d("Load Dokumen ", it.toString())
                load_dokumen(it)
            },Response.ErrorListener{
                Log.e("Load Dokumen ", it.printStackTrace().toString())
                process_load.value = 3
            })
        }
    }
    fun fetch_dokumen_offline(){
        process_load.value = 1
        try{
            val data: JSONObject = dokumenRepo.read_dokumen_storage(uuid!!)
            parse_json_to_data(data)
        }catch (exc: Exception){
            Log.d("Fetch Offline", ""+exc.printStackTrace())
            process_load.value = 3
        }
    }

    fun load_dokumen(hasil: JSONObject){
        viewModelScope.launch {
            try{
                barangRepo.berhasil_fetch_barang(hasil.getJSONArray("master_barang"))
                val data: JSONObject = hasil.getJSONObject("data")
                parse_json_to_data(data)
            }catch (e: Exception){
                process_load.value = 3
            }
        }
    }

    fun parse_json_to_data(data: JSONObject){
        prov.value = data.optInt("prov")
        kab.value = data.optInt("kab")
        kantorUnit.value = data.optInt("kantor_unit_id")
        pelabuhan.value = data.optInt("pelabuhan_id")
        jenis_pelayaran.value = data.optInt("jenis_pelayaran")

        nama_kapal.value = data.optString("nama_kapal")
        nama_kapal_1.value = data.optString("nama_kapal_1")
        bendera.value = data.optString("bendera")
        pemilik.value = data.optString("pemilik")
        nama_agen_kapal.value = data.optString("nama_agen_kapal")

        panjang_kapal.value = data.optDouble("panjang_kapal")
        panjang_grt.value = data.optDouble("panjang_grt")
        volume_nrt.value = data.optDouble("volume_nrt")
        panjang_dwt.value = data.optDouble("panjang_dwt")

        tiba_tgl.value = data.optString("tiba_tgl")
        tiba_jam.value = data.optString("tiba_jam")
        tiba_pelab_asal.value = data.optString("tiba_pelab_asal")

        tambat_tgl.value = data.optString("tambat_tgl")
        tambat_jam.value = data.optString("tambat_jam")
        tambat_jenis.value = data.optString("tambat_jenis")

        berangkat_tgl.value = data.optString("berangkat_tgl")
        berangkat_jam.value = data.optString("berangkat_jam")
        berangkat_pelab_tujuan.value = data.optString("berangkat_pelab_tujuan")

        penumpang_naik.value = data.optInt("penumpang_naik")
        penumpang_turun.value = data.optInt("penumpang_turun")

        val pdn_bongkar_value = ArrayList<perdagangan>()
        val pdn_muat_value = ArrayList<perdagangan>()
        val pln_bongkar_value = ArrayList<perdagangan>()
        val pln_muat_value = ArrayList<perdagangan>()

        for(num in (0..(data.getJSONArray("tbl_pdn_bongkar_barang").length()-1))){
            val itemData = data.getJSONArray("tbl_pdn_bongkar_barang").getJSONObject(num)
            val itemPerdagangan = perdagangan(itemData.optInt("id")
                    , itemData.optInt("barang_id")
                    , itemData.optDouble("jml")
                    , itemData.optString("satuan")
                    , itemData.optString("nama")
                    , itemData.optDouble("konversi")
                    , itemData.optInt("laporan_id"))
            pdn_bongkar_value.add(itemPerdagangan)
        }
        for(num in (0..(data.getJSONArray("tbl_pdn_muat_barang").length()-1))){
            val itemData = data.getJSONArray("tbl_pdn_muat_barang").getJSONObject(num)
            val itemPerdagangan = perdagangan(itemData.optInt("id")
                    , itemData.optInt("barang_id")
                    , itemData.optDouble("jml")
                    , itemData.optString("satuan")
                    , itemData.optString("nama")
                    , itemData.optDouble("konversi")
                    , itemData.optInt("laporan_id"))
            pdn_muat_value.add(itemPerdagangan)
        }
        for(num in (0..(data.getJSONArray("tbl_pln_bongkar_barang").length()-1))){
            val itemData = data.getJSONArray("tbl_pln_bongkar_barang").getJSONObject(num)
            val itemPerdagangan = perdagangan(itemData.optInt("id")
                    , itemData.optInt("barang_id")
                    , itemData.optDouble("jml")
                    , itemData.optString("satuan")
                    , itemData.optString("nama")
                    , itemData.optDouble("konversi")
                    , itemData.optInt("laporan_id"))
            pln_bongkar_value.add(itemPerdagangan)
        }
        for(num in (0..(data.getJSONArray("tbl_pln_muat_barang").length()-1))){
            val itemData = data.getJSONArray("tbl_pln_muat_barang").getJSONObject(num)
            val itemPerdagangan = perdagangan(itemData.optInt("id")
                    , itemData.optInt("barang_id")
                    , itemData.optDouble("jml")
                    , itemData.optString("satuan")
                    , itemData.optString("nama")
                    , itemData.optDouble("konversi")
                    , itemData.optInt("laporan_id"))
            pln_muat_value.add(itemPerdagangan)
        }
        pdn_bongkar.value = pdn_bongkar_value
        pdn_muat.value = pdn_muat_value
        pln_bongkar.value = pln_bongkar_value
        pln_muat.value = pln_muat_value

        keterangan.value = data.optString("ket")
        status.value = data.optInt("status")
        approval.value = data.optInt("approval")
        process_load.value = 2
    }

    fun parse_data_to_json(): JSONObject{
        val data = JSONObject()
        data.put("id", id)
        data.put("prov", prov.value)
        data.put("kab", kab.value)
        data.put("kantor_unit_id", kantorUnit.value)
        data.put("pelabuhan_id", pelabuhan.value)
        data.put("jenis_pelayaran", jenis_pelayaran.value)

        if(berangkat_tgl.value != null && berangkat_tgl.value != ""){
            val tanggalBerangkat = berangkat_tgl.value
            val tanggalBerangkatSplit = tanggalBerangkat!!.split("-")

            data.put("bulan", tanggalBerangkatSplit[1])
            data.put("tahun", tanggalBerangkatSplit[0])
        }

        data.put("nama_kapal", nama_kapal.value)
        data.put("nama_kapal_1", nama_kapal_1.value)
        data.put("bendera", bendera.value)
        data.put("pemilik", pemilik.value)
        data.put("nama_agen_kapal", nama_agen_kapal.value)

        data.put("panjang_kapal", panjang_kapal.value)
        data.put("panjang_grt", panjang_grt.value)
        data.put("volume_nrt", volume_nrt.value)
        data.put("panjang_dwt", panjang_dwt.value)

        data.put("tiba_tgl", tiba_tgl.value)
        data.put("tiba_jam", tiba_jam.value)
        data.put("tiba_pelab_asal", tiba_pelab_asal.value)

        data.put("tambat_tgl", tambat_tgl.value)
        data.put("tambat_jam", tambat_jam.value)
        data.put("tambat_jenis", tambat_jenis.value)

        data.put("berangkat_tgl", berangkat_tgl.value)
        data.put("berangkat_jam", berangkat_jam.value)
        data.put("berangkat_pelab_tujuan", berangkat_pelab_tujuan.value)

        data.put("penumpang_naik", penumpang_naik.value)
        data.put("penumpang_turun", penumpang_turun.value)

        val pdn_bongkar_value = JSONArray()
        val pdn_muat_value = JSONArray()
        val pln_bongkar_value = JSONArray()
        val pln_muat_value = JSONArray()

        for(num in (0..(pdn_bongkar.value!!).size-1)){
            val itemData = pdn_bongkar.value!![num].get_json()
            pdn_bongkar_value.put(itemData)
        }
        for(num in (0..(pdn_muat.value!!).size-1)){
            val itemData = pdn_muat.value!![num].get_json()
            pdn_muat_value.put(itemData)
        }
        for(num in (0..(pln_bongkar.value!!).size-1)){
            val itemData = pln_bongkar.value!![num].get_json()
            pln_bongkar_value.put(itemData)
        }
        for(num in (0..(pln_muat.value!!).size-1)){
            val itemData = pln_muat.value!![num].get_json()
            pln_muat_value.put(itemData)
        }
        data.put("tbl_pdn_bongkar_barang", pdn_bongkar_value)
        data.put("tbl_pdn_muat_barang", pdn_muat_value)
        data.put("tbl_pln_bongkar_barang", pln_bongkar_value)
        data.put("tbl_pln_muat_barang", pln_muat_value)

        data.put("ket", keterangan.value)
        data.put("status", status.value)
        data.put("approval", approval.value)
        return data
    }

    fun saveDokumen(berhasil:() -> Unit, gagal:() -> Unit){
        viewModelScope.launch {
            var pemilik_agen: String? = null

            if(pemilik.value != null && pemilik.value != "" && nama_kapal_1.value != null && nama_kapal_1.value != ""){
                pemilik_agen = pemilik.value+"/"+nama_agen_kapal.value
            }else if(pemilik.value != null && pemilik.value != ""){
                pemilik_agen = pemilik.value
            }else if(nama_kapal_1.value != null && nama_kapal_1.value != ""){
                pemilik_agen = nama_agen_kapal.value
            }

            val dokumen = Dokumen(uuid!!
                    , id!!
                    , prov.value
                    , kab.value
                    , kantorUnit.value
                    , pelabuhan.value
                    , nama_kapal_1.value
                    , nama_kapal.value
                    , nama_agen_kapal.value
                    , bendera.value
                    , pemilik_agen
                    , berangkat_tgl.value
                    , berangkat_jam.value
                    , status.value!!
                    , approval.value !!
                    , getError()
                    , System.currentTimeMillis()
            )
            try{
                dokumenRepo.write_dokumen_storage(dokumen, parse_data_to_json(), type!!)
                berhasil()
            }catch (e:Exception){
                Log.d("Simpan", ""+e.printStackTrace())
                gagal()
            }
        }
    }

    fun deleteDokumenofflineHelp(dokumen: Dokumen){
        viewModelScope.launch {
            dokumenRepo.delete_dokumen(dokumen)
        }
    }

    fun kirimDokumen(berhasil:() -> Unit, gagal:() -> Unit){
        viewModelScope.launch {
            var pemilik_agen: String? = null

            if(pemilik.value != null && pemilik.value != "" && nama_kapal_1.value != null && nama_kapal_1.value != ""){
                pemilik_agen = pemilik.value+"/"+nama_agen_kapal.value
            }else if(pemilik.value != null && pemilik.value != ""){
                pemilik_agen = pemilik.value
            }else if(nama_kapal_1.value != null && nama_kapal_1.value != ""){
                pemilik_agen = nama_agen_kapal.value
            }

            val dokumen = Dokumen(uuid!!
                    , id!!
                    , prov.value
                    , kab.value
                    , kantorUnit.value
                    , pelabuhan.value
                    , nama_kapal_1.value
                    , nama_kapal.value
                    , nama_agen_kapal.value
                    , bendera.value
                    , pemilik_agen
                    , berangkat_tgl.value
                    , berangkat_jam.value
                    , status.value!!
                    , approval.value !!
                    , getError()
                    , System.currentTimeMillis()
            )

            try{
                if(id == -1){
                    Log.d("Kirim Dokumen", parse_data_to_json().toString())
                    dokumenRepo.kirim_add_dokumen(user.value!!, parse_data_to_json(), Response.Listener<JSONObject>{
                        Log.d("Kirim Dokumen", it.toString())
                        deleteDokumenofflineHelp(dokumen)
                        berhasil()
                    },Response.ErrorListener{
                        Log.e("Kirim Dokumen", it.printStackTrace().toString())
                        gagal()
                    })
                }else{
                    dokumenRepo.kirim_edit_dokumen(user.value!!, parse_data_to_json(), Response.Listener<JSONObject>{
                        Log.d("Kirim Dokumen", it.toString())
                        deleteDokumenofflineHelp(dokumen)
                        berhasil()
                    },Response.ErrorListener{
                        Log.e("Kirim Dokumen", it.printStackTrace().toString())
                        gagal()
                    })
                }

            }catch (e:Exception){
                gagal()
            }
        }
    }

    fun getError(): Int{
        var jml_error = 0
        //blok 1
        if(prov.value == null || prov.value == 0){
            jml_error = jml_error + 1
        }
        if(kab.value == null || kab.value == 0){
            jml_error = jml_error + 1
        }
        if(kantorUnit.value == null || kantorUnit.value == 0){
            jml_error = jml_error + 1
        }
        if(pelabuhan.value == null || pelabuhan.value == 0){
            jml_error = jml_error + 1
        }
        if(jenis_pelayaran.value == null || jenis_pelayaran.value == 0){
            jml_error = jml_error + 1
        }

        // blok 2
        if(nama_kapal.value == null || nama_kapal.value == ""){
            jml_error = jml_error + 1
        }
        if(nama_kapal_1.value == null || nama_kapal_1.value == "- Pilih Jenis -"){
            jml_error = jml_error + 1
        }
        if(bendera.value == null || bendera.value == ""){
            jml_error = jml_error + 1
        }
//        if(pemilik.value == null || pemilik.value == ""){
//            jml_error = jml_error + 1
//        }
//        if(nama_agen_kapal.value == null || nama_agen_kapal.value == ""){
//            jml_error = jml_error + 1
//        }

        if((pemilik.value == null || pemilik.value == "")
                && (nama_agen_kapal.value == null || nama_agen_kapal.value == "")){
            jml_error = jml_error + 1
        }

        if(panjang_kapal.value == null || panjang_kapal.value == 0.0){
            jml_error = jml_error + 1
        }
        if(panjang_grt.value == null || panjang_grt.value == 0.0){
            jml_error = jml_error + 1
        }
        if(volume_nrt.value == null || volume_nrt.value == 0.0){
            jml_error = jml_error + 1
        }
        if(panjang_dwt.value == null || panjang_dwt.value == 0.0){
            jml_error = jml_error + 1
        }

        if(tiba_tgl.value == null || tiba_tgl.value == ""){
            jml_error = jml_error + 1
        }
        if(tiba_jam.value == null || tiba_jam.value == ""){
            jml_error = jml_error + 1
        }
        if(tiba_pelab_asal.value == null || tiba_pelab_asal.value == ""){
            jml_error = jml_error + 1
        }

        if(tambat_tgl.value == null || tambat_tgl.value == ""){
            jml_error = jml_error + 1
        }
        if(tambat_tgl.value == null || tambat_tgl.value == ""){
            jml_error = jml_error + 1
        }
        if(tambat_tgl.value == null || tambat_tgl.value == ""){
            jml_error = jml_error + 1
        }

        if(berangkat_tgl.value == null || berangkat_tgl.value == ""){
            jml_error = jml_error + 1
        }
        if(berangkat_jam.value == null || berangkat_jam.value == ""){
            jml_error = jml_error + 1
        }
        if(berangkat_pelab_tujuan.value == null || berangkat_pelab_tujuan.value == ""){
            jml_error = jml_error + 1
        }

        //tiba dan tambat
        if(tiba_tgl.value != null && tiba_tgl.value != ""
                && tiba_jam.value != null && tiba_jam.value != ""
                && tambat_tgl.value != null && tambat_tgl.value != ""
                && tambat_jam.value != null && tambat_jam.value != ""){
            if(getDateTimeString(tiba_tgl.value!!, tiba_jam.value!!)
                    >= getDateTimeString(tambat_tgl.value!!, tambat_jam.value!!)){
                jml_error = jml_error + 1
            }
        }
        //tiba dan berangkat
        if(tiba_tgl.value != null && tiba_tgl.value != ""
                && tiba_jam.value != null && tiba_jam.value != ""
                && berangkat_tgl.value != null && berangkat_tgl.value != ""
                && berangkat_jam.value != null && berangkat_jam.value != ""){
            if(getDateTimeString(tiba_tgl.value!!, tiba_jam.value!!)
                    >= getDateTimeString(berangkat_tgl.value!!, berangkat_jam.value!!)){
                jml_error = jml_error + 1
            }
        }
        //tambat dan berangkat
        if(tambat_tgl.value != null && tambat_tgl.value != ""
                && tambat_jam.value != null && tambat_jam.value != ""
                && berangkat_tgl.value != null && berangkat_tgl.value != ""
                && berangkat_jam.value != null && berangkat_jam.value != ""){
            if(getDateTimeString(tambat_tgl.value!!, tambat_jam.value!!)
                    >= getDateTimeString(berangkat_tgl.value!!, berangkat_jam.value!!)){
                jml_error = jml_error + 1
            }
        }

        return  jml_error
    }

    fun getDateTimeString(dateString: String, timeString: String): Date{
        val dateStringSplit = dateString.split("-")
        val timeStringSplit = timeString.split(":")
        return Date(dateStringSplit[2].toInt(),dateStringSplit[1].toInt()-1, dateStringSplit[0].toInt()
                , timeStringSplit[0].toInt(), timeStringSplit[1].toInt())
    }

}
