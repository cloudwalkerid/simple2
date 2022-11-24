package id.go.bps.sulbar.simple2.ui.Main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import id.go.bps.sulbar.simple2.LoginActivity
import id.go.bps.sulbar.simple2.MainActivityViewModel
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.*
import id.go.bps.sulbar.simple2.adapter.MultiSwipeRefreshLayout
import id.go.bps.sulbar.simple2.ui.PopUpWilayah


class LaporanFragment : Fragment() {

    private val model: MainActivityViewModel by activityViewModels<MainActivityViewModel>()
    private lateinit var mSwipeRefreshLayout: MultiSwipeRefreshLayout
    private lateinit var holder: LinearLayout
    private lateinit var nama_laporan: TextView
    private lateinit var unit: TextView
    private lateinit var penumpang_datang: TextView
    private lateinit var penumpang_berangkat: TextView
    private lateinit var ton_bongkar: TextView
    private lateinit var ton_muat: TextView
    private lateinit var filter: AppCompatImageButton
    private lateinit var table: TableLayout

    private var provList: ArrayList<Prov> = ArrayList()
    private var kabList: ArrayList<Kab> = ArrayList()
    private var kantorUnitList: ArrayList<Kantor_Unit> = ArrayList()
    private var pelabuhanList: ArrayList<Pelabuhan> = ArrayList()



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_main_laporan, container, false)
//        val textView: TextView = root.findViewById(R.id.text_laporan)
        //        viewLifecycleOwner
        mSwipeRefreshLayout = root.findViewById(R.id.swiperefresh)
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.primary);

        holder = root.findViewById(R.id.holder_laporan)
        nama_laporan = root.findViewById(R.id.nama_laporan)
        unit = root.findViewById(R.id.unit)
        penumpang_datang = root.findViewById(R.id.penumpang_datang);
        penumpang_berangkat = root.findViewById(R.id.penumpang_berangkat);
        ton_bongkar = root.findViewById(R.id.ton_bongkar);
        ton_muat = root.findViewById(R.id.ton_muat);
        filter = root.findViewById(R.id.filter_button)
        table = root.findViewById(R.id.tabla)
        filter.setOnClickListener(View.OnClickListener {
                    openSelection()
                });
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeRefreshLayout.setSwipeableChildren(R.id.holder_laporan);

        mSwipeRefreshLayout.setOnRefreshListener {
//            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            initiateRefresh()
        }

        model.processLaporan.observe(viewLifecycleOwner, Observer<Int> { item ->
            // Update the UI
            if(item==1){
                mSwipeRefreshLayout.setRefreshing(true);
                nama_laporan.visibility = View.GONE
                table.visibility = View.GONE
            }else if(item==2){
                nama_laporan.text = "Data Laporan\n"+model.laporanRepo.waktu.value
                unit.text = model.laporanRepo.unit.value?.toString()
                penumpang_datang.text = model.laporanRepo.penumpang_datang.value?.toString()
                penumpang_berangkat.text = model.laporanRepo.penumpang_berangkat.value?.toString()
                ton_bongkar.text = model.laporanRepo.ton_bongkar.value?.toString()
                ton_muat.text = model.laporanRepo.ton_muat.value?.toString()

                mSwipeRefreshLayout.setRefreshing(false);
                nama_laporan.visibility = View.VISIBLE
                table.visibility = View.VISIBLE
            }else if(item==3){
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, model.processLaporanPesan.value, Toast.LENGTH_LONG).show()
                nama_laporan.visibility = View.GONE
                table.visibility = View.GONE
            }
        })
        model.wilayahRepo.provLive.observe(viewLifecycleOwner, Observer<List<Prov>> { item ->
            provList = ArrayList()
            provList.add(Prov(0, "- SEMUA -"))
            provList.addAll(item)
        })
        model.wilayahRepo.kabLive.observe(viewLifecycleOwner, Observer<List<Kab>> { item ->
            kabList = ArrayList()
            kabList.add(Kab(0, "- SEMUA -", 0, "- SEMUA -"))
            kabList.addAll(item)
        })
        model.wilayahRepo.kantorUnitLive.observe(viewLifecycleOwner, Observer<List<Kantor_Unit>> { item ->
            kantorUnitList = ArrayList()
            kantorUnitList.add(Kantor_Unit(0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -"))
            kantorUnitList.addAll(item)
        })
        model.wilayahRepo.pelabuhanLive.observe(viewLifecycleOwner, Observer<List<Pelabuhan>> { item ->
            pelabuhanList = ArrayList()
            pelabuhanList.add(Pelabuhan(0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -", 0, "- SEMUA -"))
            pelabuhanList.addAll(item)
        })
//        var pertama = true
//        model.user.observe(viewLifecycleOwner, Observer<User> { item ->
//            if(item == null || item.isRefreshExpired() || item.isRefreshTTLExpired()){
//
//            }
//            if(pertama){
//                initiateRefresh()
//            }
//        })
    }

    fun initiateRefresh() {
        model.fetch_laporan()
    }

    fun onRefreshComplete(hasil: Boolean) {
        nama_laporan.visibility = View.VISIBLE
        table.visibility = View.VISIBLE

        mSwipeRefreshLayout.setRefreshing(false);
    }
    fun openSelection(){
        val dialog = this.context?.let { PopUpWilayah(it, provList, kabList, kantorUnitList, pelabuhanList, model.selection_laporan) }
        dialog!!.yesBtn.setOnClickListener {
            model.selection_laporan = dialog.getData()
            dialog.dismiss()
            model.processLaporan.value = 1
            initiateRefresh()
        }
        dialog!!.noBtn.setOnClickListener{
            dialog.dismiss()
        }
        dialog!!.show()
    }
}