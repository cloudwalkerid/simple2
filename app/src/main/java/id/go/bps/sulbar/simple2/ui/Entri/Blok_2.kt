package id.go.bps.sulbar.simple2.ui.Entri

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import id.go.bps.sulbar.simple2.EntriActivityViewModel
import id.go.bps.sulbar.simple2.R
import java.util.*


class Blok_2 : Fragment() {

    private val model: EntriActivityViewModel by activityViewModels<EntriActivityViewModel>()
    var pertama = true

    lateinit var nama_kapal: EditText
    lateinit var nama_kapal_1: Spinner
    lateinit var bendera: EditText
    lateinit var pemilik: EditText
    lateinit var nama_agen_kapal: EditText
    lateinit var panjang_kapal: EditText
    lateinit var volume_grt: EditText
    lateinit var volume_nrt: EditText
    lateinit var panjang_dwt: EditText
    lateinit var tiba_tgl: EditText
    lateinit var tiba_jam: EditText
    lateinit var tiba_pelab_asal: EditText
    lateinit var tambat_tgl: EditText
    lateinit var tambat_jam: EditText
    lateinit var tambat_jenis: EditText
    lateinit var berangkat_tgl: EditText
    lateinit var berangkat_jam: EditText
    lateinit var berangkat_pelab_tujuan: EditText
    lateinit var penumpang_naik: EditText
    lateinit var penumpang_turun: EditText

    lateinit var nama_kapal_error: TextView
    lateinit var nama_kapal_1_error: TextView
    lateinit var bendera_error: TextView
    lateinit var pemilik_error: TextView
    lateinit var nama_agen_kapal_error: TextView
    lateinit var panjang_kapal_error: TextView
    lateinit var volume_grt_error: TextView
    lateinit var volume_nrt_error: TextView
    lateinit var panjang_dwt_error: TextView
    lateinit var tiba_tgl_error: TextView
    lateinit var tiba_jam_error: TextView
    lateinit var tiba_pelab_asal_error: TextView
    lateinit var tambat_tgl_error: TextView
    lateinit var tambat_jam_error: TextView
    lateinit var tambat_jenis_error: TextView
    lateinit var berangkat_tgl_error: TextView
    lateinit var berangkat_jam_error: TextView
    lateinit var berangkat_pelab_tujuan_error: TextView
    lateinit var penumpang_naik_error: TextView
    lateinit var penumpang_turun_error: TextView

    lateinit var nama_kapal_watcher: TextWatcherBlok2String
    lateinit var bendera_watcher: TextWatcherBlok2String
    lateinit var pemilik_watcher: TextWatcherBlok2String
    lateinit var nama_agen_kapal_watcher: TextWatcherBlok2String
    lateinit var panjang_kapal_watcher: TextWatcherBlok2Double
    lateinit var volume_grt_watcher: TextWatcherBlok2Double
    lateinit var volume_nrt_watcher: TextWatcherBlok2Double
    lateinit var panjang_dwt_watcher: TextWatcherBlok2Double
    lateinit var tiba_tgl_date_picker: DatePickerBlok2
    lateinit var tiba_jam_time_picker: TimePickerBlok2
    lateinit var tiba_pelab_asal_watcher: TextWatcherBlok2String
    lateinit var tambat_tgl_date_picker: DatePickerBlok2
    lateinit var tambat_jam_time_picker: TimePickerBlok2
    lateinit var tambat_jenis_watcher: TextWatcherBlok2String
    lateinit var berangkat_tgl_date_picker: DatePickerBlok2
    lateinit var berangkat_jam_time_picker: TimePickerBlok2
    lateinit var berangkat_pelab_tujuan_watcher: TextWatcherBlok2String
    lateinit var penumpang_naik_watcher: TextWatcherBlok2Int
    lateinit var penumpang_turun_watcher: TextWatcherBlok2Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        viewLifecycleOwner
        val root = inflater.inflate(R.layout.blok_2, container, false)

        nama_kapal = root.findViewById(R.id.nama_kapal)
        nama_kapal_1 = root.findViewById(R.id.nama_kapal_1)
        bendera = root.findViewById(R.id.bendera)
        pemilik = root.findViewById(R.id.pemilik)
        nama_agen_kapal = root.findViewById(R.id.nama_agen_kapal)
        panjang_kapal = root.findViewById(R.id.panjang_kapal)
        volume_grt = root.findViewById(R.id.volume_grt)
        volume_nrt = root.findViewById(R.id.volume_nrt)
        panjang_dwt = root.findViewById(R.id.panjang_dwt)
        tiba_tgl = root.findViewById(R.id.tiba_tgl)
        tiba_jam = root.findViewById(R.id.tiba_jam)
        tiba_pelab_asal = root.findViewById(R.id.tiba_pelab_asal)
        tambat_tgl = root.findViewById(R.id.tambat_tgl)
        tambat_jam = root.findViewById(R.id.tambat_jam)
        tambat_jenis = root.findViewById(R.id.tambat_jenis)
        berangkat_tgl = root.findViewById(R.id.berangkat_tgl)
        berangkat_jam = root.findViewById(R.id.berangkat_jam)
        berangkat_pelab_tujuan = root.findViewById(R.id.berangkat_pelab_tujuan)
        penumpang_naik = root.findViewById(R.id.penumpang_naik)
        penumpang_turun = root.findViewById(R.id.penumpang_turun)

        nama_kapal_error = root.findViewById(R.id.nama_kapal_error)
        nama_kapal_1_error = root.findViewById(R.id.nama_kapal_1_error)
        bendera_error = root.findViewById(R.id.bendera_error)
        pemilik_error = root.findViewById(R.id.pemilik_error)
        nama_agen_kapal_error = root.findViewById(R.id.nama_agen_kapal_error)
        panjang_kapal_error = root.findViewById(R.id.panjang_kapal_error)
        volume_grt_error = root.findViewById(R.id.volume_grt_error)
        volume_nrt_error = root.findViewById(R.id.volume_nrt_error)
        panjang_dwt_error = root.findViewById(R.id.panjang_dwt_error)
        tiba_tgl_error = root.findViewById(R.id.tiba_tgl_error)
        tiba_jam_error = root.findViewById(R.id.tiba_jam_error)
        tiba_pelab_asal_error = root.findViewById(R.id.tiba_pelab_asal_error)
        tambat_tgl_error = root.findViewById(R.id.tambat_tgl_error)
        tambat_jam_error = root.findViewById(R.id.tambat_jam_error)
        tambat_jenis_error = root.findViewById(R.id.tambat_jenis_error)
        berangkat_tgl_error = root.findViewById(R.id.berangkat_tgl_error)
        berangkat_jam_error = root.findViewById(R.id.berangkat_jam_error)
        berangkat_pelab_tujuan_error = root.findViewById(R.id.berangkat_pelab_tujuan_error)
        penumpang_naik_error = root.findViewById(R.id.penumpang_naik_error)
        penumpang_turun_error = root.findViewById(R.id.penumpang_turun_error)

        model.process_load.observe(viewLifecycleOwner, Observer<Int>{
            if(pertama && model.process_load.value == 2){
                init_value()
                pertama = false
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pertama = true
        if(pertama && model.process_load.value == 2){
            init_value()
            pertama = false
        }
    }

    fun init_value(){
        if(!pertama){
            return
        }else{
            pertama = false
        }
        val nama_kapal_1_Adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jenis_kapal_entri)){
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
        nama_kapal_1.setAdapter(nama_kapal_1_Adapter)
        nama_kapal_1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(nama_kapal_1.selectedItemPosition == 0){
                    model.nama_kapal_1.value = null
                }else{
                    model.nama_kapal_1.value = nama_kapal_1.selectedItem as String
                }
                validasi(nama_kapal_1)
            }
        }
        if(model.nama_kapal_1.value != null){
            nama_kapal_1.setSelection(getResources().getStringArray(R.array.jenis_kapal_entri).indexOf(model.nama_kapal_1.value))
        }
        validasi(nama_kapal_1)
        nama_kapal_watcher = TextWatcherBlok2String(nama_kapal, ::validasi, model.nama_kapal)
        bendera_watcher = TextWatcherBlok2String(bendera, ::validasi, model.bendera)
        pemilik_watcher = TextWatcherBlok2String(pemilik, ::validasi, model.pemilik)
        nama_agen_kapal_watcher = TextWatcherBlok2String(nama_agen_kapal, ::validasi, model.nama_agen_kapal)
        panjang_kapal_watcher = TextWatcherBlok2Double(panjang_kapal, ::validasi, model.panjang_kapal)
        volume_grt_watcher = TextWatcherBlok2Double(volume_grt, ::validasi, model.panjang_grt)
        volume_nrt_watcher = TextWatcherBlok2Double(volume_nrt, ::validasi, model.volume_nrt)
        panjang_dwt_watcher = TextWatcherBlok2Double(panjang_dwt, ::validasi, model.panjang_dwt)
        tiba_tgl_date_picker = DatePickerBlok2(requireContext(), tiba_tgl, ::validasi, model.tiba_tgl)
        tiba_jam_time_picker = TimePickerBlok2(requireContext(), tiba_jam, ::validasi, model.tiba_jam)
        tiba_pelab_asal_watcher = TextWatcherBlok2String(tiba_pelab_asal, ::validasi, model.tiba_pelab_asal)
        tambat_tgl_date_picker = DatePickerBlok2(requireContext(), tambat_tgl, ::validasi, model.tambat_tgl)
        tambat_jam_time_picker = TimePickerBlok2(requireContext(), tambat_jam, ::validasi, model.tambat_jam)
        tambat_jenis_watcher = TextWatcherBlok2String(tambat_jenis, ::validasi, model.tambat_jenis)
        berangkat_tgl_date_picker = DatePickerBlok2(requireContext(), berangkat_tgl, ::validasi, model.berangkat_tgl)
        berangkat_jam_time_picker = TimePickerBlok2(requireContext(), berangkat_jam, ::validasi, model.berangkat_jam)
        berangkat_pelab_tujuan_watcher = TextWatcherBlok2String(berangkat_pelab_tujuan, ::validasi, model.berangkat_pelab_tujuan)
        penumpang_naik_watcher = TextWatcherBlok2Int(penumpang_naik, ::validasi, model.penumpang_naik)
        penumpang_turun_watcher = TextWatcherBlok2Int(penumpang_turun, ::validasi, model.penumpang_turun)

        tiba_tgl.setOnClickListener{
            tiba_tgl_date_picker.show()
        }
        tiba_jam.setOnClickListener{
            tiba_jam_time_picker.show()
        }
        tambat_tgl.setOnClickListener{
            tambat_tgl_date_picker.show()
        }
        tambat_jam.setOnClickListener{
            tambat_jam_time_picker.show()
        }
        berangkat_tgl.setOnClickListener{
            berangkat_tgl_date_picker.show()
        }
        berangkat_jam.setOnClickListener{
            berangkat_jam_time_picker.show()
        }

        if(model.type == 1){
            nama_kapal.isEnabled = false
            nama_kapal_1.isEnabled = false
            bendera.isEnabled = false
            pemilik.isEnabled = false
            nama_agen_kapal.isEnabled = false
            panjang_kapal.isEnabled = false
            volume_grt.isEnabled = false
            volume_nrt.isEnabled = false
            panjang_dwt.isEnabled = false
            tiba_tgl.isEnabled = false
            tiba_jam.isEnabled = false
            tiba_pelab_asal.isEnabled = false
            tambat_tgl.isEnabled = false
            tambat_jam.isEnabled = false
            tambat_jenis.isEnabled = false
            berangkat_tgl.isEnabled = false
            berangkat_jam.isEnabled = false
            berangkat_pelab_tujuan.isEnabled = false
            penumpang_naik.isEnabled = false
            penumpang_turun.isEnabled = false
        }

    }
    fun validasi(view: View){
        if(nama_kapal.text.toString() == null || nama_kapal.text.toString() == ""){
            nama_kapal_error.text = "Nama Kapal harus diisi"
            nama_kapal_error.visibility = View.VISIBLE
        }else{
            nama_kapal_error.text = ""
            nama_kapal_error.visibility = View.GONE
        }
        if(nama_kapal_1.selectedItemPosition == 0){
            nama_kapal_1_error.text = "Jenis Kapal harus diisi"
            nama_kapal_1_error.visibility = View.VISIBLE
        }else{
            nama_kapal_1_error.text = ""
            nama_kapal_1_error.visibility = View.GONE
        }
        if(bendera.text.toString() == null || bendera.text.toString() == ""){
            bendera_error.text = "Bendera harus diisi"
            bendera_error.visibility = View.VISIBLE
        }else{
            bendera_error.text = ""
            bendera_error.visibility = View.GONE
        }
        if((pemilik.text.toString() == null || pemilik.text.toString() == "")
                && (nama_agen_kapal.text.toString() == null || nama_agen_kapal.text.toString() == "")){
            pemilik_error.text = "Pemilik atau Agen harus terisi"
            pemilik_error.visibility = View.VISIBLE
            nama_agen_kapal_error.text = "Pemilik atau Agen harus terisi"
            nama_agen_kapal_error.visibility = View.VISIBLE
        }else{
            pemilik_error.text = ""
            pemilik_error.visibility = View.GONE
            nama_agen_kapal_error.text = ""
            nama_agen_kapal_error.visibility = View.GONE
        }
        if(panjang_kapal.text.toString() == null || panjang_kapal.text.toString() == "" || panjang_kapal.text.toString().toDouble() == 0.0){
            panjang_kapal_error.text = "Panjang kapal hasur terisi"
            panjang_kapal_error.visibility = View.VISIBLE
        }else{
            panjang_kapal_error.text = ""
            panjang_kapal_error.visibility = View.GONE
        }
        if(volume_grt.text.toString() == null || volume_grt.text.toString() == ""|| volume_grt.text.toString().toDouble() == 0.0){
            volume_grt_error.text = "Volume GRT harus terisi"
            volume_grt_error.visibility = View.VISIBLE
        }else{
            volume_grt_error.text = ""
            volume_grt_error.visibility = View.GONE
        }
        if(volume_nrt.text.toString() == null || volume_nrt.text.toString() == ""|| volume_nrt.text.toString().toDouble() == 0.0){
            volume_nrt_error.text = "Volume NRT harus terisi"
            volume_nrt_error.visibility = View.VISIBLE
        }else{
            volume_nrt_error.text = ""
            volume_nrt_error.visibility = View.GONE
        }
        if(panjang_dwt.text.toString() == null || panjang_dwt.text.toString() == ""|| panjang_dwt.text.toString().toDouble() == 0.0){
            panjang_dwt_error.text = "Berat DWt harus terisi"
            panjang_dwt_error.visibility = View.VISIBLE
        }else{
            panjang_dwt_error.text = ""
            panjang_dwt_error.visibility = View.GONE
        }
        if(tiba_tgl.text.toString() == null || tiba_tgl.text.toString() == ""){
            tiba_tgl_error.text = "Tiba Tanggal harus diisi"
            tiba_tgl_error.visibility = View.VISIBLE
        }else{
            tiba_tgl_error.text = ""
            tiba_tgl_error.visibility = View.GONE
        }
        if(tiba_jam.text.toString() == null || tiba_jam.text.toString() == ""){
            tiba_jam_error.text = "Tiba Jam harus diisi"
            tiba_jam_error.visibility = View.VISIBLE
        }else{
            tiba_jam_error.text = ""
            tiba_jam_error.visibility = View.GONE
        }
        if(tiba_pelab_asal.text.toString() == null || tiba_pelab_asal.text.toString() == ""){
            tiba_pelab_asal_error.text = "Pelabuhan Asal harus diisi"
            tiba_pelab_asal_error.visibility = View.VISIBLE
        }else{
            tiba_pelab_asal_error.text = ""
            tiba_pelab_asal_error.visibility = View.GONE
        }
        if(tambat_tgl.text.toString() == null || tambat_tgl.text.toString() == ""){
            tambat_tgl_error.text = "Tambat tanggal harus diisi"
            tambat_tgl_error.visibility = View.VISIBLE
        }else{
            tambat_tgl_error.text = ""
            tambat_tgl_error.visibility = View.GONE
        }
        if(tambat_jam.text.toString() == null || tambat_jam.text.toString() == ""){
            tambat_jam_error.text = "Tambat Jam harus diisi"
            tambat_jam_error.visibility = View.VISIBLE
        }else{
            tambat_jam_error.text = ""
            tambat_jam_error.visibility = View.GONE
        }
        if(tambat_jenis.text.toString() == null || tambat_jenis.text.toString() == ""){
            tambat_jenis_error.text = "Tambat Jenis harus diisi"
            tambat_jenis_error.visibility = View.VISIBLE
        }else{
            tambat_jenis_error.text = ""
            tambat_jenis_error.visibility = View.GONE
        }
        if(berangkat_tgl.text.toString() == null || berangkat_tgl.text.toString() == ""){
            berangkat_tgl_error.text = "Berangkat Tanggal harus diisi"
            berangkat_tgl_error.visibility = View.VISIBLE
        }else{
            berangkat_tgl_error.text = ""
            berangkat_tgl_error.visibility = View.GONE
        }
        if(berangkat_jam.text.toString() == null || berangkat_jam.text.toString() == ""){
            berangkat_jam_error.text = "Berangkat Jam harus diisi"
            berangkat_jam_error.visibility = View.VISIBLE
        }else{
            berangkat_jam_error.text = ""
            berangkat_jam_error.visibility = View.GONE
        }
        if(berangkat_pelab_tujuan.text.toString() == null || berangkat_pelab_tujuan.text.toString() == ""){
            berangkat_pelab_tujuan_error.text = "Pelabuhan Tujuan harus diisi"
            berangkat_pelab_tujuan_error.visibility = View.VISIBLE
        }else{
            berangkat_pelab_tujuan_error.text = ""
            berangkat_pelab_tujuan_error.visibility = View.GONE
        }
        if(penumpang_naik.text.toString() == null || penumpang_naik.text.toString() == "" ){
            penumpang_naik_error.text = "Penumpang naik harus diisi"
            penumpang_naik_error.visibility = View.VISIBLE
        }else{
            penumpang_naik_error.text = ""
            penumpang_naik_error.visibility = View.GONE
        }
        if(penumpang_turun.text.toString() == null || penumpang_turun.text.toString() == ""){
            penumpang_turun_error.text = "Penumpang turun harus diisi"
            penumpang_turun_error.visibility = View.VISIBLE
        }else{
            penumpang_turun_error.text = ""
            penumpang_turun_error.visibility = View.GONE
        }

        //tiba dan tambat
        if(tiba_tgl.text.toString() != null && tiba_tgl.text.toString() != ""
                && tiba_jam.text.toString() != null && tiba_jam.text.toString() != ""
                && tambat_tgl.text.toString() != null && tambat_tgl.text.toString() != ""
                && tambat_jam.text.toString() != null && tambat_jam.text.toString() != ""){
            if(getDateTimeString(tiba_tgl.text.toString(), tiba_jam.text.toString())
                    >= getDateTimeString(tambat_tgl.text.toString(), tambat_jam.text.toString())){
                tiba_tgl_error.text = "Waktu tiba harus lebih kecil dari waktu tambat"
                tiba_tgl_error.visibility = View.VISIBLE
                tiba_jam_error.text = "Waktu tiba harus lebih kecil dari waktu tambat"
                tiba_jam_error.visibility = View.VISIBLE
                tambat_tgl_error.text = "Waktu tiba harus lebih kecil dari waktu tambat"
                tambat_tgl_error.visibility = View.VISIBLE
                tambat_jam_error.text = "Waktu tiba harus lebih kecil dari waktu tambat"
                tambat_jam_error.visibility = View.VISIBLE
            }else{
                tiba_tgl_error.text = ""
                tiba_tgl_error.visibility = View.GONE
                tiba_jam_error.text = ""
                tiba_jam_error.visibility = View.GONE
                tambat_tgl_error.text = ""
                tambat_tgl_error.visibility = View.GONE
                tambat_jam_error.text = ""
                tambat_jam_error.visibility = View.GONE
            }
        }
        //tiba dan berangkat
        if(tiba_tgl.text.toString() != null && tiba_tgl.text.toString() != ""
                && tiba_jam.text.toString() != null && tiba_jam.text.toString() != ""
                && berangkat_tgl.text.toString() != null && berangkat_tgl.text.toString() != ""
                && berangkat_jam.text.toString() != null && berangkat_jam.text.toString() != ""){
            if(getDateTimeString(tiba_tgl.text.toString(), tiba_jam.text.toString())
                    >= getDateTimeString(berangkat_tgl.text.toString(), berangkat_jam.text.toString())){
                tiba_tgl_error.text = "Waktu tiba harus lebih kecil dari waktu berangkat"
                tiba_tgl_error.visibility = View.VISIBLE
                tiba_jam_error.text = "Waktu tiba harus lebih kecil dari waktu berangkat"
                tiba_jam_error.visibility = View.VISIBLE
                berangkat_tgl_error.text = "Waktu tiba harus lebih kecil dari waktu berangkat"
                berangkat_tgl_error.visibility = View.VISIBLE
                berangkat_jam_error.text = "Waktu tiba harus lebih kecil dari waktu berangkat"
                berangkat_jam_error.visibility = View.VISIBLE
            }else{
                tiba_tgl_error.text = ""
                tiba_tgl_error.visibility = View.GONE
                tiba_jam_error.text = ""
                tiba_jam_error.visibility = View.GONE
                berangkat_tgl_error.text = ""
                berangkat_tgl_error.visibility = View.GONE
                berangkat_jam_error.text = ""
                berangkat_jam_error.visibility = View.GONE
            }
        }
        //tambat dan berangkat
        if(tambat_tgl.text.toString() != null && tambat_tgl.text.toString() != ""
                && tambat_jam.text.toString() != null && tambat_jam.text.toString() != ""
                && berangkat_tgl.text.toString() != null && berangkat_tgl.text.toString() != ""
                && berangkat_jam.text.toString() != null && berangkat_jam.text.toString() != ""){
            if(getDateTimeString(tambat_tgl.text.toString(), tambat_jam.text.toString())
                    >= getDateTimeString(berangkat_tgl.text.toString(), berangkat_jam.text.toString())){
                tambat_tgl_error.text = "Waktu tambat harus lebih kecil dari waktu berangkat"
                tambat_tgl_error.visibility = View.VISIBLE
                tambat_jam_error.text = "Waktu tambat harus lebih kecil dari waktu berangkat"
                tambat_jam_error.visibility = View.VISIBLE
                berangkat_tgl_error.text = "Waktu tambat harus lebih kecil dari waktu berangkat"
                berangkat_tgl_error.visibility = View.VISIBLE
                berangkat_jam_error.text = "Waktu tambat harus lebih kecil dari waktu berangkat"
                berangkat_jam_error.visibility = View.VISIBLE
            }else{
                tambat_tgl_error.text = ""
                tambat_tgl_error.visibility = View.GONE
                tambat_jam_error.text = ""
                tambat_jam_error.visibility = View.GONE
                berangkat_tgl_error.text = ""
                berangkat_tgl_error.visibility = View.GONE
                berangkat_jam_error.text = ""
                berangkat_jam_error.visibility = View.GONE
            }
        }
    }

    class TimePickerBlok2(val context: Context, val editText: EditText, val validasi: (View) -> Unit, val liveData: MutableLiveData<String>): TimePickerDialog.OnTimeSetListener {
        var haourDayPub: Int = 0
        var minutePub : Int = 0
        lateinit var dialog: TimePickerDialog
        init {
            // Use the current time as the default values for the picker
            if(liveData.value == null || liveData.value == ""){
                val c = Calendar.getInstance()
                val hour = c.get(Calendar.HOUR_OF_DAY)
                val minute = c.get(Calendar.MINUTE)

                haourDayPub = hour
                minutePub = minute

                // Create a new instance of TimePickerDialog and return it
                dialog = TimePickerDialog(context, this, haourDayPub, minutePub, DateFormat.is24HourFormat(context))
            }else{
                val valueSplit = liveData.value!!.split(":")
                haourDayPub = valueSplit[0].toInt()
                minutePub = valueSplit[1].toInt()
                editText.setText("${haourDayPub.toString().padStart(2, '0')}:${minutePub.toString().padStart(2, '0')}")
                dialog = TimePickerDialog(context, this, haourDayPub, minutePub, DateFormat.is24HourFormat(context))
            }
            validasi(editText)
        }

        fun show(){
            dialog.show()
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            // Do something with the time chosen by the user
            haourDayPub = hourOfDay
            minutePub = minute
            editText.setText("${haourDayPub.toString().padStart(2, '0')}:${minutePub.toString().padStart(2, '0')}")
            liveData.value = "${haourDayPub.toString().padStart(2, '0')}:${minutePub.toString().padStart(2, '0')}"
            validasi(editText)
        }
    }

    class DatePickerBlok2(val context: Context, val editText: EditText, val validasi: (View) -> Unit, val liveData: MutableLiveData<String>) : DatePickerDialog.OnDateSetListener {
        var dayPub: Int = 0
        var monthPub: Int = 0
        var yearPub : Int = 0
        lateinit var dialog: DatePickerDialog
        init{
            // Use the current date as the default date in the picker
            if(liveData.value == null || liveData.value == ""){
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                dayPub = day
                monthPub = month
                yearPub = year

                // Create a new instance of DatePickerDialog and return it
                dialog = DatePickerDialog(context, this, yearPub, monthPub, dayPub)
            }else{
                val valueSplit = liveData.value!!.split("-")
                dayPub = valueSplit[2].toInt()
                monthPub = valueSplit[1].toInt() - 1
                yearPub = valueSplit[0].toInt()

                editText.setText("${dayPub.toString().padStart(2, '0')}-${(monthPub+1).toString().padStart(2, '0')}-$yearPub")

                // Create a new instance of DatePickerDialog and return it
                dialog = DatePickerDialog(context, this, yearPub, monthPub, dayPub)
            }
            validasi(editText)
        }

        fun show(){
            dialog.show()
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            dayPub = day
            monthPub = month + 1
            yearPub = year

            editText.setText("${dayPub.toString().padStart(2, '0')}-${monthPub.toString().padStart(2, '0')}-$yearPub")
            liveData.value = "$yearPub-$monthPub-$dayPub"
            validasi(editText)

        }
    }

    class TextWatcherBlok2String(val view: EditText, val validasi: (View) -> Unit, val liveData: MutableLiveData<String>): TextWatcher{

        init {
            view.addTextChangedListener(this)
            if(liveData.value!=null){
                view.setText(liveData.value)
            }
            validasi(view)
        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            Log.d("Watcher", ""+s.toString())
//            liveData.value = s
//            Log.d("Watcher1", ""+liveData.value.toString())
            if(s==null){
                liveData.value = null
                validasi(view)
            }else{
                liveData.value = s.toString()
                validasi(view)
            }
        }
    }
    class TextWatcherBlok2Int(val view: EditText, val validasi: (View) -> Unit, val liveData: MutableLiveData<Int>): TextWatcher{

        init {
            view.addTextChangedListener(this)
            if(liveData.value!=null){
                view.setText(liveData.value.toString())
            }
            validasi(view)
        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d("Watcher", ""+s.toString())
//            liveData.value = s
//            Log.d("Watcher1", ""+liveData.value.toString())
            if(s==null || s.toString()==""){
                liveData.value = null
                validasi(view)
            }else{
                liveData.value = s.toString().toInt()
                validasi(view)
            }
        }
    }
    class TextWatcherBlok2Double(val view: EditText, val validasi: (View) -> Unit, val liveData: MutableLiveData<Double>): TextWatcher{

        init {
            view.addTextChangedListener(this)
            if(liveData.value!=null){
                view.setText(liveData.value.toString())
            }
            validasi(view)
        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s==null || s.toString()==""){
                liveData.value = null
                validasi(view)
            }else{
                liveData.value = s.toString().toDouble()
                validasi(view)
            }
        }
    }

    fun getDateTimeString(dateString: String, timeString: String): Date{
        val dateStringSplit = dateString.split("-")
        val timeStringSplit = timeString.split(":")
        return Date(dateStringSplit[2].toInt(),dateStringSplit[1].toInt()-1, dateStringSplit[0].toInt()
                , timeStringSplit[0].toInt(), timeStringSplit[1].toInt())
    }


}