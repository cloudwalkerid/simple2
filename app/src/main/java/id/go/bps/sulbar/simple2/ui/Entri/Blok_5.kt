package id.go.bps.sulbar.simple2.ui.Entri

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import id.go.bps.sulbar.simple2.EntriActivityViewModel
import id.go.bps.sulbar.simple2.R


class Blok_5 : Fragment() {

    private val model: EntriActivityViewModel by activityViewModels<EntriActivityViewModel>()
    var pertama = true

    lateinit var ket: EditText
    lateinit var status: RadioGroup
    lateinit var status_1: RadioButton
    lateinit var status_0: RadioButton

    lateinit var ket_error: TextView
    lateinit var status_error: TextView
    var selectedStatus: Int = 0

    lateinit var simpanButton: AppCompatButton
    lateinit var kirimButton: AppCompatButton

    lateinit var error_all: TextView
    lateinit var error_simpan: TextView

    lateinit var ketWatcher: TextWatcherBlok5String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        viewLifecycleOwner
        val root = inflater.inflate(R.layout.blok_5, container, false)

        ket = root.findViewById(R.id.ket)
        status = root.findViewById(R.id.status)
        status_1 = root.findViewById(R.id.status_1)
        status_0 = root.findViewById(R.id.status_0)

        ket_error = root.findViewById(R.id.ket_error)
        status_error = root.findViewById(R.id.status_error)

        simpanButton = root.findViewById(R.id.simpan_button)
        kirimButton = root.findViewById(R.id.kirim_button)

        error_simpan = root.findViewById(R.id.error_simpan)
        error_all = root.findViewById(R.id.error_all)

        model.process_load.observe(viewLifecycleOwner, Observer<Int>{
            if(pertama && model.process_load.value == 2){
                init_value()
                pertama = false
            }
        })

        simpanButton.setOnClickListener{
            model.saveDokumen(::berhasil_simpan, ::gagal_simpan)
        }
        kirimButton.setOnClickListener {
            model.kirimDokumen(::berhasil_kirim, ::gagal_kirim)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(model.type == 1){
            error_all.visibility = View.GONE
            error_simpan.visibility = View.GONE
            return
        }
        if(model.prov.value !=null && model.prov.value != 0 && model.kab.value !=null && model.kab.value != 0
                && model.kantorUnit.value !=null && model.kantorUnit.value != 0 && model.pelabuhan.value !=null && model.pelabuhan.value != 0
                && model.nama_kapal_1.value !=null && model.nama_kapal_1.value != "- Pilih Jenis -"
                && model.nama_kapal.value !=null && model.nama_kapal.value != ""){
            error_simpan.text = ""
            error_simpan.visibility = View.GONE
            simpanButton.visibility = View.VISIBLE
        }else{
            error_simpan.text = "Informasi tempat, nama, dan jenis kapal tidak boleh kosong"
            error_simpan.visibility = View.VISIBLE
            simpanButton.visibility = View.GONE
        }

        val jml_error: Int = model.getError()
        if(jml_error == 0){
            kirimButton.visibility = View.VISIBLE
            error_all.text =""
            error_all.visibility = View.GONE
        }else{
            kirimButton.visibility = View.GONE
            error_all.text = "Terdapat "+jml_error+" error"
            error_all.visibility = View.VISIBLE
        }
    }

    fun init_value(){
        ketWatcher = TextWatcherBlok5String(ket, model.keterangan)


        status.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener()
        { radioGroup: RadioGroup, i: Int ->
                when(i){
                    R.id.status_1->{
                        selectedStatus = 1
                        model.status.value = 1
                    }
                    R.id.status_0->{
                        model.status.value = 0
                    }
                }
        });
        if(model.status.value == 1){
            status.check(R.id.status_1)
        }else if (model.status.value == 0){
            status.check(R.id.status_0)
        }
        if(model.type == 1){
            ket.isEnabled = false
            status.isEnabled = false
            status_0.isEnabled = false
            status_1.isEnabled = false
            simpanButton.visibility = View.GONE
            kirimButton.visibility = View.GONE
        }
    }

    fun berhasil_simpan(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        alertDialogBuilder.setMessage("Berhasil menyimpan data !!")
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_check_circle_24)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
            dialog.dismiss()
            activity?.finish()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun gagal_simpan(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        alertDialogBuilder.setMessage("Gagal menyimpan data !!")
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_not_interested_24)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun berhasil_kirim(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        alertDialogBuilder.setMessage("Berhasil mengirim data !!")
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_check_circle_24)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
            dialog.dismiss()
            activity?.finish()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun gagal_kirim(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        alertDialogBuilder.setMessage("Gagal mengirim data !!")
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_not_interested_24)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    class TextWatcherBlok5String(val view: EditText, val liveData: MutableLiveData<String>): TextWatcher{

        init {
            view.addTextChangedListener(this)
            if(liveData.value!=null){
                view.setText(liveData.value)
            }
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
            }else{
                liveData.value = s.toString()
            }
        }
    }
}