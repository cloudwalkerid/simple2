package id.go.bps.sulbar.simple2.`object`

import org.json.JSONObject

data class perdagangan (
        var id: Int,
        var barang_id: Int,
        var jml: Double,
        var satuan: String,
        var nama: String,
        var konversi: Double,
        var laporan_id: Int){
    fun get_json(): JSONObject{
        val data = JSONObject()
        data.put("id",id)
        data.put("barang_id",barang_id)
        data.put("jml",jml)
        data.put("satuan",satuan)
        data.put("nama", nama)
        data.put("konversi",konversi)
        data.put("laporan_id",laporan_id)
        return data
    }
}