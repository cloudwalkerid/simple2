package id.go.bps.sulbar.simple2.ui.Entri

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.observe
import id.go.bps.sulbar.simple2.LoginActivityViewModel
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.Barang
import id.go.bps.sulbar.simple2.`object`.Kantor_Unit
import id.go.bps.sulbar.simple2.`object`.perdagangan
import kotlin.math.roundToLong


class TambahPerdaganganActivity : AppCompatActivity() {

    val viewModel: TambahPerdaganganActivityModel by viewModels()
    lateinit var barang_id: AutoCompleteTextView
    lateinit var nama_barang: EditText
    var barang_id_pos:Int = -1
    lateinit var satuan: Spinner
    lateinit var jumlah: EditText

    lateinit var barang_id_error: TextView
    lateinit var nama_barang_error: TextView
    lateinit var satuan_error: TextView
    lateinit var jumlah_error: TextView
    lateinit var tambah: AppCompatButton

    private var list_barang: ArrayList<Barang> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_perdagangan)

        barang_id = findViewById(R.id.barang_id)
        barang_id.threshold = 1
        nama_barang = findViewById(R.id.nama_barang)
        satuan = findViewById(R.id.satuan)
        jumlah = findViewById(R.id.jumlah)
        tambah = findViewById(R.id.tambah)
        tambah.visibility = View.GONE

        barang_id_error = findViewById(R.id.barang_id_error)
        nama_barang_error = findViewById(R.id.nama_barang_error)
        satuan_error = findViewById(R.id.satuan_error)
        jumlah_error = findViewById(R.id.jumlah_error)

        viewModel.barangs.observe(this){
            list_barang = ArrayList()
            list_barang.addAll(it)
            setBarangAdapter()
        }

        tambah.setOnClickListener{
            result()
        }
        barang_id.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            Log.d("barang 11", "pos:"+i)
            val barangSelected = adapterView.getItemAtPosition(i) as Barang
            barang_id_pos = list_barang.indexOf(barangSelected)
            setSatuanAdapter(barangSelected.getSatuanList())
            nama_barang.setText(barangSelected.barang)
            validation(barang_id)
            barang_id.isEnabled = false
        }

        satuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                validation(satuan)
            }
        }
        nama_barang.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validation(nama_barang)
            }
        })
        jumlah.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validation(jumlah)
            }
        })

        validation(barang_id)
        validation(satuan)
        validation(nama_barang)
        validation(jumlah)
    }
    fun setBarangAdapter(){
        val spinnerBarangArrayAdapter: ArrayAdapter<Barang> = object : ArrayAdapter<Barang>(this,
            android.R.layout.simple_spinner_item, list_barang){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView

                // set spinner item padding
                view.gravity = Gravity.CENTER
                view.setPadding(
                    10, // left
                    7, // top
                    10, // right
                    7 // bottom
                )
                return view
            }
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView

                // set spinner item padding
                view.gravity = Gravity.CENTER
                view.setPadding(
                    10, // left
                    7, // top
                    10, // right
                    7 // bottom
                )
                return view
            }
        }
        barang_id.setAdapter(spinnerBarangArrayAdapter)

    }
    fun setSatuanAdapter(satuan_nama: List<String>){
        val spinnerSatuanArrayAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, satuan_nama){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView

                // set spinner item padding
                view.gravity = Gravity.CENTER
                view.setPadding(
                    10, // left
                    7, // top
                    10, // right
                    7 // bottom
                )
                return view
            }
        }
        satuan.setAdapter(spinnerSatuanArrayAdapter)
    }
    fun validation(item: View){
        if (item == barang_id){
            if(barang_id_pos == -1){
                barang_id_error.text = "Jenis Barang Harus Diisi dengan pilih"
                barang_id_error.visibility = View.VISIBLE
            }else{
                barang_id_error.text = ""
                barang_id_error.visibility = View.GONE
            }
        }else if(item == satuan){
            if(satuan.selectedItem == null){
                satuan_error.text = "Satuan Harus Diisi"
                satuan_error.visibility = View.VISIBLE
            }else{
                satuan_error.text = ""
                satuan_error.visibility = View.GONE
            }
        }else if(item == nama_barang){
            if(nama_barang.text == null || nama_barang.text.equals("")){
                nama_barang_error.text = "Nama Barang Harus Diisi"
                nama_barang_error.visibility = View.VISIBLE
            }else{
                nama_barang_error.text = ""
                nama_barang_error.visibility = View.GONE
            }
        }else if(item == jumlah){
            if(jumlah.text == null || jumlah.text.toString() == "" || jumlah.text.toString().toDouble() == 0.0){
                jumlah_error.text = "Jumlah Harus Diisi"
                jumlah_error.visibility = View.VISIBLE
            }else{
                jumlah_error.text = ""
                jumlah_error.visibility = View.GONE
            }
        }
        if(barang_id_error.visibility == View.GONE && satuan_error.visibility == View.GONE
            && nama_barang_error.visibility == View.GONE && jumlah_error.visibility == View.GONE){
            tambah.visibility = View.VISIBLE
        }else{
            tambah.visibility = View.GONE
        }
    }
    fun result(){
        val intent = Intent()
        intent.putExtra("id", 0)
        intent.putExtra("barang_id", list_barang[barang_id_pos].id)
        intent.putExtra("jml", jumlah.text.toString().toDouble())
        intent.putExtra("satuan", list_barang[barang_id_pos].getSatuanList()[satuan.selectedItemPosition])
        intent.putExtra("nama", nama_barang.text.toString())
        intent.putExtra("konversi",  (list_barang[barang_id_pos].getKonversiList()[satuan.selectedItemPosition]*jumlah.text.toString().toDouble()))
        intent.putExtra("laporan_id", 0)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}