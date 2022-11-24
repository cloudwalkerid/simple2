package id.go.bps.sulbar.simple2.ui.Entri

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import id.go.bps.sulbar.simple2.EntriActivityViewModel
import id.go.bps.sulbar.simple2.MainActivityViewModel
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.Kab
import id.go.bps.sulbar.simple2.`object`.Kantor_Unit
import id.go.bps.sulbar.simple2.`object`.Pelabuhan
import id.go.bps.sulbar.simple2.`object`.Prov
import kotlin.collections.ArrayList

class Blok_1 : Fragment() {

    private val model: EntriActivityViewModel by activityViewModels<EntriActivityViewModel>()

    lateinit var provSpinner: Spinner
    lateinit var kabSpinner: Spinner
    lateinit var kantorUnitSpinner: Spinner
    lateinit var pelabuhanSpinner: Spinner
    lateinit var jenisPelayaranSpinner: Spinner

    lateinit var provError: TextView
    lateinit var kabError: TextView
    lateinit var kantorUnitError: TextView
    lateinit var pelabuhanError: TextView
    lateinit var jenisPelayaranError: TextView

    private var provList: ArrayList<Prov> = ArrayList()
    private var kabList: ArrayList<Kab> = ArrayList()
    private var kantorUnitList: ArrayList<Kantor_Unit> = ArrayList()
    private var pelabuhanList: ArrayList<Pelabuhan> = ArrayList()

    var provListSelect: ArrayList<Prov> = ArrayList()
    var kabListSelect: ArrayList<Kab> = ArrayList()
    var kantorUnitListSelect: ArrayList<Kantor_Unit> = ArrayList()
    var pelabuhanListSelect: ArrayList<Pelabuhan> = ArrayList()
    var pertama = true

    lateinit var spinnerProvArrayAdapter: ArrayAdapter<Prov>
    lateinit var spinnerKabArrayAdapter: ArrayAdapter<Kab>
    lateinit var spinnerKantorUnitArrayAdapter: ArrayAdapter<Kantor_Unit>
    lateinit var spinnerPelabuhanArrayAdapter: ArrayAdapter<Pelabuhan>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewLifecycleOwner
        val root = inflater.inflate(R.layout.blok_1, container, false)

        provSpinner = root.findViewById(R.id.prov_spinner)
        kabSpinner = root.findViewById(R.id.kab_spinner)
        kantorUnitSpinner = root.findViewById(R.id.kantor_unit_spinner)
        pelabuhanSpinner = root.findViewById(R.id.pelabuhan_spinner)
        jenisPelayaranSpinner = root.findViewById(R.id.pelayaran_spinner)

        provError = root.findViewById(R.id.prov_error)
        kabError = root.findViewById(R.id.kab_error)
        kantorUnitError = root.findViewById(R.id.kantor_unit_error)
        pelabuhanError = root.findViewById(R.id.pelabuhan_error)
        jenisPelayaranError = root.findViewById(R.id.pelayaran_error)

        model.process_load.observe(viewLifecycleOwner, Observer<Int>{
            if(pertama && model.process_load.value == 2 && provList.size>0 && kabList.size>0 && kantorUnitList.size>0 && pelabuhanList.size>0 ){
                init_value()
                pertama = false
            }
        })

        model.wilayahRepo.provLive.observe(viewLifecycleOwner, Observer<List<Prov>> { item ->
            provList = ArrayList()
            provList.add(Prov(0, "- PILIH PROVINSI -"))
            provList.addAll(item)
            provListSelect.add(Prov(0, "- PILIH PROVINSI -"))
            provListSelect.addAll(item)
            if(pertama && model.process_load.value == 2 && provList.size>0 && kabList.size>0 && kantorUnitList.size>0 && pelabuhanList.size>0 ){
                init_value()
                pertama = false
            }
        })
        model.wilayahRepo.kabLive.observe(viewLifecycleOwner, Observer<List<Kab>> { item ->
            kabList = ArrayList()
            kabList.add(Kab(0, "- PILIH KABUPATEN -", 0, "- PILIH KABUPATEN -"))
            kabList.addAll(item)
            kabListSelect.add(Kab(0, "- PILIH KABUPATEN -", 0, "- PILIH KABUPATEN -"))
            kabListSelect.addAll(item)
            if(pertama && model.process_load.value == 2 && provList.size>0 && kabList.size>0 && kantorUnitList.size>0 && pelabuhanList.size>0 ){
                init_value()
                pertama = false
            }
        })
        model.wilayahRepo.kantorUnitLive.observe(viewLifecycleOwner, Observer<List<Kantor_Unit>> { item ->
            kantorUnitList = ArrayList()
            kantorUnitList.add(Kantor_Unit(0, "- PILIH KANTOR UNIT -", 0, "- PILIH KANTOR UNIT  -", 0, "- PILIH KANTOR UNIT  -"))
            kantorUnitList.addAll(item)
            kantorUnitListSelect.add(Kantor_Unit(0, "- PILIH KANTOR UNIT -", 0, "- PILIH KANTOR UNIT  -", 0, "- PILIH KANTOR UNIT  -"))
            kantorUnitListSelect.addAll(item)
            if(pertama && model.process_load.value == 2 && provList.size>0 && kabList.size>0 && kantorUnitList.size>0 && pelabuhanList.size>0 ){
                init_value()
                pertama = false
            }
        })
        model.wilayahRepo.pelabuhanLive.observe(viewLifecycleOwner, Observer<List<Pelabuhan>> { item ->
            pelabuhanList = ArrayList()
            pelabuhanList.add(Pelabuhan(0, "- PILIH PELABUHAN -", 0, "- PILIH PELABUHAN -", 0, "- PILIH PELABUHAN -", 0, "- PILIH PELABUHAN -"))
            pelabuhanList.addAll(item)
            pelabuhanListSelect.add(Pelabuhan(0, "- PILIH PELABUHAN -", 0, "- PILIH PELABUHAN -", 0, "- PILIH PELABUHAN -", 0, "- PILIH PELABUHAN -"))
            pelabuhanListSelect.addAll(item)
            if(pertama && model.process_load.value == 2 && provList.size>0 && kabList.size>0 && kantorUnitList.size>0 && pelabuhanList.size>0 ){
                init_value()
                pertama = false
            }
        })

        return root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)

        pertama = true

        if(pertama && model.process_load.value == 2 && provList.size>0 && kabList.size>0 && kantorUnitList.size>0 && pelabuhanList.size>0 ){
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
        spinnerProvArrayAdapter = object  : ArrayAdapter<Prov>(requireContext(),
                android.R.layout.simple_spinner_item, provListSelect){
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

        provSpinner.setAdapter(spinnerProvArrayAdapter)

        spinnerKabArrayAdapter = object : ArrayAdapter<Kab>(requireContext(),
                android.R.layout.simple_spinner_item, kabListSelect){
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
        kabSpinner.setAdapter(spinnerKabArrayAdapter)

        spinnerKantorUnitArrayAdapter = object : ArrayAdapter<Kantor_Unit>(requireContext(),
                android.R.layout.simple_spinner_item, kantorUnitListSelect){
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
        kantorUnitSpinner.setAdapter(spinnerKantorUnitArrayAdapter)

        spinnerPelabuhanArrayAdapter = object : ArrayAdapter<Pelabuhan>(requireContext(),
                android.R.layout.simple_spinner_item, pelabuhanListSelect){
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
        pelabuhanSpinner.setAdapter(spinnerPelabuhanArrayAdapter)

        val jenisPelayaranAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jenis_pelayaran_entri)){
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
        jenisPelayaranSpinner.setAdapter(jenisPelayaranAdapter)

        provSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                wilayah_onselect_item()
            }
        }

        kabSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                wilayah_onselect_item()
            }
        }
        kantorUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                wilayah_onselect_item()
            }
        }

        pelabuhanSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                wilayah_onselect_item()
            }
        }

        jenisPelayaranSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               validasi(jenisPelayaranSpinner)
            }
        }

        provListSelect.forEachIndexed{index, item: Prov ->
            if(item.id == model.prov.value){
                provSpinner.setSelection(index)
            }
        }
       kabListSelect.forEachIndexed{index, item: Kab ->
            if(item.kab_id == model.kab.value){
                kabSpinner.setSelection(index)
            }
        }
        kantorUnitListSelect.forEachIndexed{index, item: Kantor_Unit ->
            if(item.kantor_unit_id == model.kantorUnit.value){
                kantorUnitSpinner.setSelection(index)
            }
        }
        pelabuhanListSelect.forEachIndexed{index, item: Pelabuhan ->
            if(item.pelabuhan_id == model.pelabuhan.value){
                pelabuhanSpinner.setSelection(index)
            }
        }
        jenisPelayaranSpinner.setSelection(model.jenis_pelayaran.value!!)
        validasi(provSpinner)
        validasi(kabSpinner)
        validasi(kantorUnitSpinner)
        validasi(pelabuhanSpinner)
        validasi(jenisPelayaranSpinner)

        if(model.type == 1){
            provSpinner.isEnabled = false
            kabSpinner.isEnabled = false
            kantorUnitSpinner.isEnabled = false
            pelabuhanSpinner.isEnabled = false
            jenisPelayaranSpinner.isEnabled = false
        }
    }

    fun validasi(view: View){
        if(view == provSpinner){
            model.prov.value = (provSpinner.selectedItem as Prov).id
            if((provSpinner.selectedItem as Prov).id == 0 ){
                provError.setText("Provinsi harus dipilih")
                provError.visibility = View.VISIBLE
            }else{
                provError.setText("")
                provError.visibility = View.GONE
            }
        }else if(view == kabSpinner){
            model.kab.value = (kabSpinner.selectedItem as Kab).kab_id
            if((kabSpinner.selectedItem as Kab).kab_id == 0 ){
                kabError.setText("Kabupaten harus dipilih")
                kabError.visibility = View.VISIBLE
            }else{
                kabError.setText("")
                kabError.visibility = View.GONE
            }
        }else if(view == kantorUnitSpinner){
            model.kantorUnit.value = (kantorUnitSpinner.selectedItem as Kantor_Unit).kantor_unit_id
            if((kantorUnitSpinner.selectedItem as Kantor_Unit).kantor_unit_id == 0 ){
                kantorUnitError.setText("Kantor Unit harus dipilih")
                kantorUnitError.visibility = View.VISIBLE
            }else{
                kantorUnitError.setText("")
                kantorUnitError.visibility = View.GONE
            }
        }else if(view == pelabuhanSpinner){
            model.pelabuhan.value = (pelabuhanSpinner.selectedItem as Pelabuhan).pelabuhan_id
            if((pelabuhanSpinner.selectedItem as Pelabuhan).pelabuhan_id == 0 ){
                pelabuhanError.setText("Pelabuhan harus dipilih")
                pelabuhanError.visibility = View.VISIBLE
            }else{
                pelabuhanError.setText("")
                pelabuhanError.visibility = View.GONE
            }
        }else if(view == jenisPelayaranSpinner){
            model.jenis_pelayaran.value = jenisPelayaranSpinner.selectedItemPosition
            if(jenisPelayaranSpinner.selectedItemPosition == 0 ){
                jenisPelayaranError.setText("Jenis pelayaran harus dipilih")
                jenisPelayaranError.visibility = View.VISIBLE
            }else{
                jenisPelayaranError.setText("")
                jenisPelayaranError.visibility = View.GONE
            }
        }
    }


    fun wilayah_onselect_item(){
        var provSpinnerValue: Prov = provSpinner.selectedItem as Prov
        var kabSpinnerValue: Kab = kabSpinner.selectedItem as Kab
        var kantorUnitSpinnerValue: Kantor_Unit = kantorUnitSpinner.selectedItem as Kantor_Unit
        var pelabuhanSpinnerValue: Pelabuhan = pelabuhanSpinner.selectedItem as Pelabuhan

        provListSelect = ArrayList<Prov>(getSelectionProv((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
        kabListSelect = ArrayList<Kab>(getSelectionKab((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
        kantorUnitListSelect = ArrayList<Kantor_Unit>(getSelectionKantorUnit((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
        pelabuhanListSelect = ArrayList<Pelabuhan>(getSelectionPelabunan((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))

        if(provListSelect.size == 1){
            provSpinnerValue = provListSelect[0]
        }
        if(kabListSelect.size == 1){
            kabSpinnerValue == kabListSelect[0]
        }
        if(kantorUnitListSelect.size == 1){
            kantorUnitSpinnerValue = kantorUnitListSelect[0]
        }
        if(pelabuhanListSelect.size == 1){
            pelabuhanSpinnerValue = pelabuhanListSelect[0]
        }

        provListSelect = ArrayList<Prov>(getSelectionProv(provSpinnerValue, kabSpinnerValue, kantorUnitSpinnerValue, pelabuhanSpinnerValue))
        kabListSelect = ArrayList<Kab>(getSelectionKab(provSpinnerValue, kabSpinnerValue, kantorUnitSpinnerValue, pelabuhanSpinnerValue))
        kantorUnitListSelect = ArrayList<Kantor_Unit>(getSelectionKantorUnit(provSpinnerValue, kabSpinnerValue, kantorUnitSpinnerValue, pelabuhanSpinnerValue))
        pelabuhanListSelect = ArrayList<Pelabuhan>(getSelectionPelabunan(provSpinnerValue, kabSpinnerValue, kantorUnitSpinnerValue, pelabuhanSpinnerValue))

        spinnerProvArrayAdapter.clear()
        spinnerProvArrayAdapter.addAll(provListSelect)
        spinnerProvArrayAdapter.notifyDataSetChanged()

        spinnerKabArrayAdapter.clear()
        spinnerKabArrayAdapter.addAll(kabListSelect)
        spinnerKabArrayAdapter.notifyDataSetChanged()

        spinnerKantorUnitArrayAdapter.clear()
        spinnerKantorUnitArrayAdapter.addAll(kantorUnitListSelect)
        spinnerKantorUnitArrayAdapter.notifyDataSetChanged()

        spinnerPelabuhanArrayAdapter.clear()
        spinnerPelabuhanArrayAdapter.addAll(pelabuhanListSelect)
        spinnerPelabuhanArrayAdapter.notifyDataSetChanged()

        if(provListSelect.size == 1){
            provSpinner.setSelection(0)
        }else if (provSpinnerValue != null) {
            provSpinner.setSelection(provListSelect.indexOf(provSpinnerValue))
        }
        if(kabListSelect.size == 1){
            kabSpinner.setSelection(0)
        }else if (kabSpinnerValue != null) {
            kabSpinner.setSelection(kabListSelect.indexOf(kabSpinnerValue))
        }
        if(kantorUnitListSelect.size == 1){
            kantorUnitSpinner.setSelection(0)
        }else if (kantorUnitSpinnerValue != null) {
            kantorUnitSpinner.setSelection(kantorUnitListSelect.indexOf(kantorUnitSpinnerValue))
        }
        if(pelabuhanListSelect.size == 1){
            pelabuhanSpinner.setSelection(0)
        }else if (pelabuhanSpinnerValue != null) {
            pelabuhanSpinner.setSelection(pelabuhanListSelect.indexOf(pelabuhanSpinnerValue))
        }

        validasi(provSpinner)
        validasi(kabSpinner)
        validasi(kantorUnitSpinner)
        validasi(pelabuhanSpinner)
    }

    fun getSelectionProv(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Prov>{
        return provList.filter {
            (it.id == 0 && kab.kab_id == 0 && kantorUnit.kantor_unit_id == 0 && pelabuhan.pelabuhan_id == 0) ||
                    ((it.id == kab.prov_id || kab.prov_id==0) &&
                    (it.id == kantorUnit.prov_id || kantorUnit.prov_id==0) &&
                    (it.id == pelabuhan.prov_id || pelabuhan.prov_id==0))
        }
    }
    fun getSelectionKab(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Kab>{
        return kabList.filter {
            (it.kab_id == 0 && kantorUnit.kantor_unit_id == 0 && pelabuhan.pelabuhan_id == 0) ||
                    ((it.prov_id == prov.id || prov.id==0) &&
                    (it.kab_id == kantorUnit.kab_id || kantorUnit.kab_id==0) &&
                    (it.kab_id == pelabuhan.kab_id || pelabuhan.kab_id==0))
        }
    }
    fun getSelectionKantorUnit(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Kantor_Unit>{
        return kantorUnitList.filter {
            (it.kantor_unit_id == 0 && pelabuhan.pelabuhan_id == 0) ||
                    ((it.prov_id == prov.id || prov.id==0) &&
                    (it.kab_id == kab.kab_id || kab.kab_id==0) &&
                    (it.kantor_unit_id == pelabuhan.kantor_unit_id || pelabuhan.kantor_unit_id==0))
        }
    }
    fun getSelectionPelabunan(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Pelabuhan>{
        return pelabuhanList.filter {
            it.pelabuhan_id == 0 ||
                    ((it.prov_id == prov.id || prov.id==0) &&
                    (it.kab_id == kab.kab_id || kab.kab_id==0) &&
                    (it.kantor_unit_id == kantorUnit.kantor_unit_id || kantorUnit.kantor_unit_id==0))
        }
    }
    
}