package id.go.bps.sulbar.simple2.ui.Main

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.go.bps.sulbar.simple2.EntriActivity
import id.go.bps.sulbar.simple2.MainActivityViewModel
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.*
import id.go.bps.sulbar.simple2.adapter.DataOfflineAdapter
import id.go.bps.sulbar.simple2.adapter.DataOnlineAdapter
import id.go.bps.sulbar.simple2.adapter.MultiSwipeRefreshLayout
import id.go.bps.sulbar.simple2.ui.PopUpWilayah


class EntriFragmentOnline : Fragment() {

    private val model: MainActivityViewModel by activityViewModels<MainActivityViewModel>()
    private lateinit var mSwipeRefreshLayout: MultiSwipeRefreshLayout

    private var provList: ArrayList<Prov> = ArrayList()
    private var kabList: ArrayList<Kab> = ArrayList()
    private var kantorUnitList: ArrayList<Kantor_Unit> = ArrayList()
    private var pelabuhanList: ArrayList<Pelabuhan> = ArrayList()
    private var dokumenOfflineList: ArrayList<Dokumen> = ArrayList()
    private lateinit var filter: AppCompatImageButton

    private lateinit var online_list: RecyclerView
    private lateinit var onlineAdapter: DataOnlineAdapter
    private lateinit var select_spinner: Spinner
    private lateinit var online_tidak_ada: TextView
    var pertama: Boolean = true


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //        viewLifecycleOwner
        val root = inflater.inflate(R.layout.fragment_main_entri_online, container, false)
//        val textView: TextView = root.findViewById(R.id.text_entri)
        mSwipeRefreshLayout = root.findViewById(R.id.swiperefresh)
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.primary);

        online_list = root.findViewById(R.id.online_holder)
        filter = root.findViewById(R.id.filter_button)
        select_spinner = root.findViewById(R.id.select_spinner)
        online_tidak_ada = root.findViewById(R.id.online_tidak_ada)

        filter.setOnClickListener(View.OnClickListener {
            openSelection()
        });

        val onlinerecyce: RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        online_list.setLayoutManager(onlinerecyce)

        online_list.setNestedScrollingEnabled(false)

        val jenisSelectAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jenis_select)){
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
        select_spinner.setAdapter(jenisSelectAdapter)

        select_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                select()
            }
        }

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeRefreshLayout.setSwipeableChildren(R.id.offline_holder, R.id.online_holder);

        model.processListDoku.observe(viewLifecycleOwner, Observer<Int> { item ->
            // Update the UI
            if(item==1){
                mSwipeRefreshLayout.setRefreshing(true);
            }else if(item==2){
                mSwipeRefreshLayout.setRefreshing(false);
            }else if(item==3){
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, model.processListDokuPesan.value, Toast.LENGTH_LONG).show()
            }
        })

        model.user.observe(viewLifecycleOwner, Observer {
            if(pertama && model.user.value != null){
                onlineAdapter = DataOnlineAdapter(::editOnlineDokumen
                        , ::deleteOnlineDokumen
                        , ::lihatOnlineDokumen
                        , ::approveOnlineDokumen
                        , ::getProvList
                        , ::getKabList
                        , ::getKantorUnitList
                        , ::getPelabuhanList
                        , ::getOfflineDokumen
                        , model.user.value!!)
                online_list.adapter = onlineAdapter
                init_listen_doku()
                pertama = false
            }
        })



        mSwipeRefreshLayout.setOnRefreshListener {
//            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            initiateRefresh()
        }


    }

    fun init_listen_doku(){
        model.listDokumenRepo.list_dokumen_online.observe(viewLifecycleOwner, Observer<ArrayList<Dokumen>>{
//            if(it.size>0){
//                Toast.makeText(context, "Berhasil Dapat : "+it.size, Toast.LENGTH_LONG).show()
//                onlineAdapter.submitList(it)
//                onlineAdapter.notifyDataSetChanged()
//            }
            select()
        })
        model.listDokumenRepo.list_dokumen_offline.observe(viewLifecycleOwner, Observer<List<Dokumen>>{
            if(it != null){
                dokumenOfflineList.addAll(it)
                onlineAdapter.notifyDataSetChanged()
            }
        })
        model.wilayahRepo.provLive.observe(viewLifecycleOwner, Observer<List<Prov>> { item ->
            provList = ArrayList()
            provList.add(Prov(0, "- SEMUA -"))
            provList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                onlineAdapter.notifyDataSetChanged()
            }
        })
        model.wilayahRepo.kabLive.observe(viewLifecycleOwner, Observer<List<Kab>> { item ->
            kabList = ArrayList()
            kabList.add(Kab(0, "- SEMUA -", 0, "- SEMUA -"))
            kabList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                onlineAdapter.notifyDataSetChanged()
            }
        })
        model.wilayahRepo.kantorUnitLive.observe(viewLifecycleOwner, Observer<List<Kantor_Unit>> { item ->
            kantorUnitList = ArrayList()
            kantorUnitList.add(Kantor_Unit(0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -"))
            kantorUnitList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                onlineAdapter.notifyDataSetChanged()
            }
        })
        model.wilayahRepo.pelabuhanLive.observe(viewLifecycleOwner, Observer<List<Pelabuhan>> { item ->
            pelabuhanList = ArrayList()
            pelabuhanList.add(Pelabuhan(0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -"))
            pelabuhanList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                onlineAdapter.notifyDataSetChanged()
            }
        })
    }

    fun initiateRefresh() {
        model.fetch_dokumen_list()
    }

    fun lihatOnlineDokumen(oneDokumen: Dokumen){
        val intent = Intent(context, EntriActivity::class.java)
        intent.putExtra("TYPE", 1)
        intent.putExtra("ID", oneDokumen.id)
        intent.putExtra("UUID", oneDokumen.uuid)
        startActivity(intent)
    }
    fun editOnlineDokumen(oneDokumen: Dokumen){
        val intent = Intent(context, EntriActivity::class.java)
        intent.putExtra("TYPE", 3)
        intent.putExtra("ID", oneDokumen.id)
        intent.putExtra("UUID", oneDokumen.uuid)
        startActivity(intent)
    }
    fun deleteOnlineDokumen(oneDokumen: Dokumen){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        if(oneDokumen.nama_kapal_1 == null || oneDokumen.nama_kapal == null){
            alertDialogBuilder.setMessage("Hapus Dokumen ?")
        }else{
            alertDialogBuilder.setMessage("Hapus Dokumen "+oneDokumen.nama_kapal_1+" "+oneDokumen.nama_kapal+"?")
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Ya") { dialog, id ->
            dialog.dismiss()
            model.deleteDokumenOnline(oneDokumen, {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Info")
                alertDialogBuilder.setMessage("Berhasil menghapus dokumen!!")
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_check_circle_24)
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
                model.fetch_dokumen_list()
            }, {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Info")
                alertDialogBuilder.setMessage("Gagal menghapus dokumen!!")
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_not_interested_24)
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
            })
        }
        alertDialogBuilder.setNegativeButton("Batal") { dialog, id ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun approveOnlineDokumen(oneDokumen: Dokumen){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        if(oneDokumen.nama_kapal_1 == null || oneDokumen.nama_kapal == null){
            alertDialogBuilder.setMessage("Approve Dokumen ?")
        }else{
            alertDialogBuilder.setMessage("Approve Dokumen "+oneDokumen.nama_kapal_1+" "+oneDokumen.nama_kapal+"?")
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Ya") { dialog, id ->
            dialog.dismiss()
            model.approveDokumen(oneDokumen, {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Info")
                alertDialogBuilder.setMessage("Berhasil approve dokumen!!")
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_check_circle_24)
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
                model.fetch_dokumen_list()
            }, {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Info")
                alertDialogBuilder.setMessage("Gagal approve dokumen!!")
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_not_interested_24)
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
            })
        }
        alertDialogBuilder.setNegativeButton("Batal") { dialog, id ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun getProvList(): List<Prov>{
        return provList
    }
    fun getKabList(): List<Kab>{
        return kabList
    }
    fun getKantorUnitList(): List<Kantor_Unit>{
        return kantorUnitList
    }
    fun getPelabuhanList(): List<Pelabuhan>{
        return pelabuhanList
    }

    fun getOfflineDokumen(): List<Dokumen>{
        return dokumenOfflineList
    }

    fun openSelection(){
        val dialog = this.context?.let { PopUpWilayah(it, provList, kabList, kantorUnitList, pelabuhanList, model.selection_list_dokumen) }
        dialog!!.yesBtn.setOnClickListener {
            model.selection_list_dokumen = dialog.getData()
            dialog.dismiss()
            model.processListDoku.value = 1
            initiateRefresh()
        }
        dialog!!.noBtn.setOnClickListener{
            dialog.dismiss()
        }
        dialog!!.show()
    }

    fun select(){
        val position: Int = select_spinner.selectedItemPosition
        if(model.listDokumenRepo.list_dokumen_online.value == null){
            online_tidak_ada.visibility = View.VISIBLE
        }else{
            online_tidak_ada.visibility = View.GONE
            when(position){
                0 -> {
                    //semua
                    val list_object = model.listDokumenRepo.list_dokumen_online.value!!
                    if(list_object.size>0){
                        online_tidak_ada.visibility = View.GONE
                    }else{
                        online_tidak_ada.visibility = View.VISIBLE
                    }
                    onlineAdapter.submitList(list_object)
                    onlineAdapter.notifyDataSetChanged()
                    Toast.makeText(context, "Terdapat "+list_object.size+" dokumen", Toast.LENGTH_LONG).show()
                }
                1 -> {
                    // sudah approve
                    val list_object = model.listDokumenRepo.list_dokumen_online.value!!.filter { it.approval == 1 && it.status == 1 }
                    if(list_object.size>0){
                        online_tidak_ada.visibility = View.GONE
                    }else{
                        online_tidak_ada.visibility = View.VISIBLE
                    }
                    onlineAdapter.submitList(list_object)
                    onlineAdapter.notifyDataSetChanged()
                    Toast.makeText(context, "Terdapat "+list_object.size+" dokumen", Toast.LENGTH_LONG).show()
                }
                2 -> {
                    // belum approve
                    val list_object = model.listDokumenRepo.list_dokumen_online.value!!.filter { it.approval == 0 && it.status == 1 }
                    if(list_object.size>0){
                        online_tidak_ada.visibility = View.GONE
                    }else{
                        online_tidak_ada.visibility = View.VISIBLE
                    }
                    onlineAdapter.submitList(list_object)
                    onlineAdapter.notifyDataSetChanged()
                    Toast.makeText(context, "Terdapat "+list_object.size+" dokumen", Toast.LENGTH_LONG).show()
                }
                3 -> {
                    //belum selesai
                    val list_object = model.listDokumenRepo.list_dokumen_online.value!!.filter { it.approval == 0 && it.status == 0 }
                    if(list_object.size>0){
                        online_tidak_ada.visibility = View.GONE
                    }else{
                        online_tidak_ada.visibility = View.VISIBLE
                    }
                    onlineAdapter.submitList(list_object)
                    onlineAdapter.notifyDataSetChanged()
                    Toast.makeText(context, "Terdapat "+list_object.size+" dokumen", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}