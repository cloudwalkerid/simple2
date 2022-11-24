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
import androidx.lifecycle.observe
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
import java.util.*
import kotlin.collections.ArrayList

class EntriFragmentOffline  : Fragment() {

    private val model: MainActivityViewModel by activityViewModels<MainActivityViewModel>()

    private var provList: ArrayList<Prov> = ArrayList()
    private var kabList: ArrayList<Kab> = ArrayList()
    private var kantorUnitList: ArrayList<Kantor_Unit> = ArrayList()
    private var pelabuhanList: ArrayList<Pelabuhan> = ArrayList()

    private lateinit var offline_list: RecyclerView
    private lateinit var offlineAdapter: DataOfflineAdapter
    private lateinit var fabs: FloatingActionButton
    private lateinit var offline_tidak_ada: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //        viewLifecycleOwner
        val root = inflater.inflate(R.layout.fragment_main_entri_offline, container, false)
//        val textView: TextView = root.findViewById(R.id.text_entri)

        offline_list = root.findViewById(R.id.offline_holder)
        fabs = root.findViewById(R.id.tambah)
        fabs.setOnClickListener(View.OnClickListener {
            tambahOfflineDokumen()
        })

        val offlinerecyce: RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        offline_list.setLayoutManager(offlinerecyce)

        offlineAdapter = DataOfflineAdapter(::editOfflineDokumen
                , ::deleteOfflineDokumen
                , ::kirimOfflineDokumen
                , ::getProvList
                , ::getKabList
                , ::getKantorUnitList
                , ::getPelabuhanList)
        offline_list.adapter = offlineAdapter

        offline_tidak_ada = root.findViewById(R.id.offline_tidak_ada)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.wilayahRepo.provLive.observe(viewLifecycleOwner, Observer<List<Prov>> { item ->
            provList = ArrayList()
            provList.add(Prov(0, "- SEMUA -"))
            provList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                offlineAdapter.notifyDataSetChanged()
            }
        })
        model.wilayahRepo.kabLive.observe(viewLifecycleOwner, Observer<List<Kab>> { item ->
            kabList = ArrayList()
            kabList.add(Kab(0, "- SEMUA -", 0, "- SEMUA -"))
            kabList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                offlineAdapter.notifyDataSetChanged()
            }
        })
        model.wilayahRepo.kantorUnitLive.observe(viewLifecycleOwner, Observer<List<Kantor_Unit>> { item ->
            kantorUnitList = ArrayList()
            kantorUnitList.add(Kantor_Unit(0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -"))
            kantorUnitList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                offlineAdapter.notifyDataSetChanged()
            }
        })
        model.wilayahRepo.pelabuhanLive.observe(viewLifecycleOwner, Observer<List<Pelabuhan>> { item ->
            pelabuhanList = ArrayList()
            pelabuhanList.add(Pelabuhan(0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -"))
            pelabuhanList.addAll(item)
            if(provList.size > 0 && kabList.size > 0 && kantorUnitList.size > 0 && pelabuhanList.size > 0){
                offlineAdapter.notifyDataSetChanged()
            }
        })


        model.listDokumenRepo.list_dokumen_offline.observe(viewLifecycleOwner, Observer<List<Dokumen>>{
            if(it != null){
                offlineAdapter.submitList(model.listDokumenRepo.list_dokumen_offline.value)
                offlineAdapter.notifyDataSetChanged()
                if(it.isEmpty()){
                    offline_tidak_ada.visibility = View.VISIBLE
                }else{
                    offline_tidak_ada.visibility = View.GONE
                }
            }
        })
    }


    fun tambahOfflineDokumen(){
        val intent = Intent(context, EntriActivity::class.java)
        intent.putExtra("TYPE", 0)
        intent.putExtra("ID", -1)
        intent.putExtra("UUID", UUID.randomUUID().toString())
        startActivity(intent)
    }

    fun editOfflineDokumen(oneDokumen: Dokumen){
        val intent = Intent(context, EntriActivity::class.java)
        intent.putExtra("TYPE", 2)
        intent.putExtra("ID", oneDokumen.id)
        intent.putExtra("UUID", oneDokumen.uuid)
        startActivity(intent)
    }
    fun deleteOfflineDokumen(oneDokumen: Dokumen){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        alertDialogBuilder.setMessage("Hapus Dokumen "+oneDokumen.nama_kapal_1+" "+oneDokumen.nama_kapal+"?")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Ya") { dialog, id ->
            dialog.dismiss()
            model.deleteDokumenoffline(oneDokumen, {
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
    fun kirimOfflineDokumen(oneDokumen: Dokumen){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Info")
        alertDialogBuilder.setMessage("Kirim Dokumen "+oneDokumen.nama_kapal_1+" "+oneDokumen.nama_kapal+"?")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Ya") { dialog, id ->
            dialog.dismiss()
            model.kirimDokumenoffline(oneDokumen, {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Info")
                alertDialogBuilder.setMessage("Berhasil mengirim dokumen!!")
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_check_circle_24)
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setNeutralButton("OK") { dialog, id ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }, {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Info")
                alertDialogBuilder.setMessage("Gagal mengirim dokumen!!")
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

}