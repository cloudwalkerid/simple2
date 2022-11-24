package id.go.bps.sulbar.simple2.ui

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.*
import java.util.*
import kotlin.collections.ArrayList

class PopUpWilayah constructor(
        val context2: Context,
        val provList: List<Prov>,
        val kabList: List<Kab>,
        val kantorUnitList: List<Kantor_Unit>,
        val pelabuhanList: List<Pelabuhan>,
        val selection: filter
)  : Dialog(context2) {

    var provSpinner: Spinner
    var kabSpinner: Spinner
    var kantorUnitSpinner: Spinner
    var pelabuhanSpinner: Spinner
    var bulanSpinner: Spinner
    var tahunSpinner: Spinner
    var yesBtn: Button
    var noBtn: Button

    var provListSelect: ArrayList<Prov> = ArrayList(provList)
    var kabListSelect: ArrayList<Kab> = ArrayList(kabList)
    var kantorUnitListSelect: ArrayList<Kantor_Unit> = ArrayList(kantorUnitList)
    var pelabuhanListSelect: ArrayList<Pelabuhan> = ArrayList(pelabuhanList)
    var arrayTahun: ArrayList<String> = ArrayList();

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.popup_wilayah)

        provSpinner = findViewById<Spinner>(R.id.prov_spinner)
        kabSpinner = findViewById<Spinner>(R.id.kab_spinner)
        kantorUnitSpinner = findViewById<Spinner>(R.id.kantor_unit_spinner)
        pelabuhanSpinner = findViewById<Spinner>(R.id.pelabuhan_spinner)
        bulanSpinner = findViewById<Spinner>(R.id.bulan_spinner)
        tahunSpinner = findViewById<Spinner>(R.id.tahun_spinner)
        yesBtn = findViewById<Button>(R.id.simpan_button)
        noBtn = findViewById<Button>(R.id.batal_button)

        val spinnerProvArrayAdapter: ArrayAdapter<Prov> = object  : ArrayAdapter<Prov>(context,
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

        val spinnerKabArrayAdapter: ArrayAdapter<Kab> = object : ArrayAdapter<Kab>(context,
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

        val spinnerKantorUnitArrayAdapter: ArrayAdapter<Kantor_Unit> = object : ArrayAdapter<Kantor_Unit>(context,
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

        val spinnerPelabuhanArrayAdapter: ArrayAdapter<Pelabuhan> = object : ArrayAdapter<Pelabuhan>(context,
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

        val spinnerbulanArrayAdapter = ArrayAdapter.createFromResource(
                context, R.array.bulan, android.R.layout.simple_spinner_item).also { adapter -> // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            bulanSpinner.adapter = adapter
        }
        bulanSpinner.setAdapter(spinnerbulanArrayAdapter)

        for(num in (((Date()).year + 1900) downTo 2018)){
            arrayTahun.add(num.toString())
        }

        Log.d("Tahun", arrayTahun.toString())

        val spinnerTahunAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arrayTahun.toTypedArray()){
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
        tahunSpinner.setAdapter(spinnerTahunAdapter)

        // selection and on itemselected

        bulanSpinner.setSelection(selection.bulan-1)

        var tahunPilih = 0
        for(num in (((Date()).year + 1900) downTo 2018)){
            if(num == selection.tahun){
                tahunSpinner.setSelection(tahunPilih)
            }
            tahunPilih = tahunPilih + 1
        }

        provSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Laporan", "Fire Prov Spinner 1")
                val kabSpinnerValue: Kab = kabSpinner.selectedItem as Kab
                val kantorUnitSpinnerValue: Kantor_Unit = kantorUnitSpinner.selectedItem as Kantor_Unit
                val pelabuhanSpinnerValue: Pelabuhan = pelabuhanSpinner.selectedItem as Pelabuhan

                kabListSelect = ArrayList<Kab>(getSelectionKab((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                kantorUnitListSelect = ArrayList<Kantor_Unit>(getSelectionKantorUnit((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                pelabuhanListSelect = ArrayList<Pelabuhan>(getSelectionPelabunan((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))

                spinnerKabArrayAdapter.clear()
                spinnerKabArrayAdapter.addAll(kabListSelect)
                spinnerKabArrayAdapter.notifyDataSetChanged()

                spinnerKantorUnitArrayAdapter.clear()
                spinnerKantorUnitArrayAdapter.addAll(kantorUnitListSelect)
                spinnerKantorUnitArrayAdapter.notifyDataSetChanged()

                spinnerPelabuhanArrayAdapter.clear()
                spinnerPelabuhanArrayAdapter.addAll(pelabuhanListSelect)
                spinnerPelabuhanArrayAdapter.notifyDataSetChanged()

                if (kabSpinnerValue != null) {
                    kabSpinner.setSelection(kabListSelect.indexOf(kabSpinnerValue))
                }
                if (kantorUnitSpinnerValue != null) {
                    kantorUnitSpinner.setSelection(kantorUnitListSelect.indexOf(kantorUnitSpinnerValue))
                }
                if (pelabuhanSpinnerValue != null) {
                    pelabuhanSpinner.setSelection(pelabuhanListSelect.indexOf(pelabuhanSpinnerValue))
                }
            }
        }

        kabSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Laporan", "Fire Kab Spinner")
                val provSpinnerValue: Prov = provSpinner.selectedItem as Prov
                val kantorUnitSpinnerValue: Kantor_Unit = kantorUnitSpinner.selectedItem as Kantor_Unit
                val pelabuhanSpinnerValue: Pelabuhan = pelabuhanSpinner.selectedItem as Pelabuhan

                provListSelect = ArrayList<Prov>(getSelectionProv((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                kantorUnitListSelect = ArrayList<Kantor_Unit>(getSelectionKantorUnit((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                pelabuhanListSelect = ArrayList<Pelabuhan>(getSelectionPelabunan((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))

                spinnerProvArrayAdapter.clear()
                spinnerProvArrayAdapter.addAll(provListSelect)
                spinnerProvArrayAdapter.notifyDataSetChanged()

                spinnerKantorUnitArrayAdapter.clear()
                spinnerKantorUnitArrayAdapter.addAll(kantorUnitListSelect)
                spinnerKantorUnitArrayAdapter.notifyDataSetChanged()

                spinnerPelabuhanArrayAdapter.clear()
                spinnerPelabuhanArrayAdapter.addAll(pelabuhanListSelect)
                spinnerPelabuhanArrayAdapter.notifyDataSetChanged()

                if(provSpinnerValue != null){
                    provSpinner.setSelection(provListSelect.indexOf(provSpinnerValue))
                }
                if(kantorUnitSpinnerValue != null){
                    kantorUnitSpinner.setSelection(kantorUnitListSelect.indexOf(kantorUnitSpinnerValue))
                }
                if(pelabuhanSpinnerValue != null){
                    pelabuhanSpinner.setSelection(pelabuhanListSelect.indexOf(pelabuhanSpinnerValue))
                }
            }
        }
        kantorUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Laporan", "Fire Kantor Unit Spinner")
                val provSpinnerValue: Prov = provSpinner.selectedItem as Prov
                val kabSpinnerValue: Kab = kabSpinner.selectedItem as Kab
                val pelabuhanSpinnerValue: Pelabuhan = pelabuhanSpinner.selectedItem as Pelabuhan

                provListSelect = ArrayList<Prov>(getSelectionProv((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                kabListSelect = ArrayList<Kab>(getSelectionKab((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                pelabuhanListSelect = ArrayList<Pelabuhan>(getSelectionPelabunan((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))

                spinnerProvArrayAdapter.clear()
                spinnerProvArrayAdapter.addAll(provListSelect)
                spinnerProvArrayAdapter.notifyDataSetChanged()

                spinnerKabArrayAdapter.clear()
                spinnerKabArrayAdapter.addAll(kabListSelect)
                spinnerKabArrayAdapter.notifyDataSetChanged()

                spinnerPelabuhanArrayAdapter.clear()
                spinnerPelabuhanArrayAdapter.addAll(pelabuhanListSelect)
                spinnerPelabuhanArrayAdapter.notifyDataSetChanged()

                if(provSpinnerValue != null){
                    provSpinner.setSelection(provListSelect.indexOf(provSpinnerValue))
                }
                if(kabSpinnerValue != null){
                    kabSpinner.setSelection(kabListSelect.indexOf(kabSpinnerValue))
                }
                if(pelabuhanSpinnerValue != null){
                    pelabuhanSpinner.setSelection(pelabuhanListSelect.indexOf(pelabuhanSpinnerValue))
                }
            }
        }

        pelabuhanSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Laporan", "Fire Pelabuhan Spinner")
                val provSpinnerValue: Prov = provSpinner.selectedItem as Prov
                val kabSpinnerValue: Kab = kabSpinner.selectedItem as Kab
                val kantorUnitSpinnerValue: Kantor_Unit = kantorUnitSpinner.selectedItem as Kantor_Unit

                provListSelect = ArrayList<Prov>(getSelectionProv((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                kabListSelect = ArrayList<Kab>(getSelectionKab((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))
                kantorUnitListSelect = ArrayList<Kantor_Unit>(getSelectionKantorUnit((provSpinner.selectedItem as Prov), (kabSpinner.selectedItem as Kab)
                        , (kantorUnitSpinner.selectedItem as Kantor_Unit), (pelabuhanSpinner.selectedItem as Pelabuhan)))

                spinnerProvArrayAdapter.clear()
                spinnerProvArrayAdapter.addAll(provListSelect)
                spinnerProvArrayAdapter.notifyDataSetChanged()

                spinnerKabArrayAdapter.clear()
                spinnerKabArrayAdapter.addAll(kabListSelect)
                spinnerKabArrayAdapter.notifyDataSetChanged()

                spinnerKantorUnitArrayAdapter.clear()
                spinnerKantorUnitArrayAdapter.addAll(kantorUnitListSelect)
                spinnerKantorUnitArrayAdapter.notifyDataSetChanged()

                if(provSpinnerValue != null){
                    kabSpinner.setSelection(provListSelect.indexOf(provSpinnerValue))
                }
                if(kabSpinnerValue != null){
                    kabSpinner.setSelection(kabListSelect.indexOf(kabSpinnerValue))
                }
                if(kantorUnitSpinnerValue != null){
                    kantorUnitSpinner.setSelection(kantorUnitListSelect.indexOf(kantorUnitSpinnerValue))
                }
            }
        }

        for(num in (0..provList.size-1)){
            val provItem = provList.get(num)
            if(provItem.id == selection.prov){
                provSpinner.setSelection(num)
                break;
            }
        }
        for(num in (0..kabList.size-1)){
            val kabItem = kabList.get(num)
            if(kabItem.kab_id == selection.kab){
                kabSpinner.setSelection(num)
                break
            }
        }
        for(num in (0..kantorUnitList.size-1)){
            val kUnitItem = kantorUnitList.get(num)
            if(kUnitItem.kantor_unit_id == selection.kantorUnit){
                kantorUnitSpinner.setSelection(num)
                break
            }
        }
        for(num in (0..pelabuhanList.size-1)){
            val pelabuhanItem = pelabuhanList.get(num)
            if(pelabuhanItem.pelabuhan_id == selection.pelabuhan){
                pelabuhanSpinner.setSelection(num)
                break
            }
        }
    }

    fun getData(): filter{
        selection.prov = (provSpinner.selectedItem as Prov).id
        selection.kab = (kabSpinner.selectedItem as Kab).kab_id
        selection.kantorUnit = (kantorUnitSpinner.selectedItem as Kantor_Unit).kantor_unit_id
        selection.pelabuhan = (pelabuhanSpinner.selectedItem as Pelabuhan).pelabuhan_id
        selection.bulan = bulanSpinner.selectedItemPosition + 1
        Log.d("Laporan", arrayTahun.get(tahunSpinner.selectedItemPosition))
        selection.tahun = (tahunSpinner.selectedItem as String).toInt()
        return selection
    }



    fun getSelectionProv(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Prov>{
        return provList.filter {
            it.id == 0 || ((it.id == kab.prov_id || kab.prov_id==0) &&
                    (it.id == kantorUnit.prov_id || kantorUnit.prov_id==0) &&
                    (it.id == pelabuhan.prov_id || pelabuhan.prov_id==0))
        }
    }
    fun getSelectionKab(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Kab>{
        return kabList.filter {
            it.kab_id == 0 || ((it.prov_id == prov.id || prov.id==0) &&
                    (it.kab_id == kantorUnit.kab_id || kantorUnit.kab_id==0) &&
                    (it.kab_id == pelabuhan.kab_id || pelabuhan.kab_id==0))
        }
    }
    fun getSelectionKantorUnit(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Kantor_Unit>{
        return kantorUnitList.filter {
            it.kantor_unit_id == 0 || ((it.prov_id == prov.id || prov.id==0) &&
                    (it.kab_id == kab.kab_id || kab.kab_id==0) &&
                    (it.kantor_unit_id == pelabuhan.kantor_unit_id || pelabuhan.kantor_unit_id==0))
        }
    }
    fun getSelectionPelabunan(prov: Prov, kab: Kab, kantorUnit: Kantor_Unit, pelabuhan: Pelabuhan): List<Pelabuhan>{
        return pelabuhanList.filter {
            it.pelabuhan_id == 0 || ((it.prov_id == prov.id ||prov.id==0) &&
                    (it.kab_id == kab.kab_id ||  kab.kab_id==0) &&
                    (it.kantor_unit_id == kantorUnit.kantor_unit_id ||  kantorUnit.kantor_unit_id==0))
        }
    }
}