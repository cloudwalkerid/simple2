package id.go.bps.sulbar.simple2.ui.Entri

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.go.bps.sulbar.simple2.EntriActivityViewModel
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.perdagangan
import id.go.bps.sulbar.simple2.adapter.PerdaganganBarangAdapter

class Blok_4_1 : Fragment() {

    private val model: EntriActivityViewModel by activityViewModels<EntriActivityViewModel>()
    var pertama = true

    lateinit var perdagangan_holder: RecyclerView
    lateinit var tambah: AppCompatButton

    lateinit var perdaganganAdapter: PerdaganganBarangAdapter
    lateinit var perdaganganIni: ArrayList<perdagangan>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        viewLifecycleOwner
        val root = inflater.inflate(R.layout.blok_4_1, container, false)
        perdagangan_holder = root.findViewById(R.id.perdagangan_holder)
        tambah = root.findViewById(R.id.tambah_perdagangan)
        tambah.visibility = View.GONE
        tambah.setOnClickListener{
            val masukTambah = Intent(activity, TambahPerdaganganActivity::class.java)
            startActivityForResult(masukTambah, 1)
        }

        val onlinerecyce: RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        perdagangan_holder.setLayoutManager(onlinerecyce)

        perdaganganAdapter = PerdaganganBarangAdapter(::hapus, model.type!!)
        perdagangan_holder.adapter = perdaganganAdapter

        model.process_load.observe(viewLifecycleOwner, Observer<Int>{
            if(pertama && model.process_load.value == 2){
                init_value()
                pertama = false
            }
        })

        model.pln_bongkar.observe(viewLifecycleOwner, Observer<ArrayList<perdagangan>>{
            perdaganganIni = it
            perdaganganAdapter.submitList((perdaganganIni))
            perdaganganAdapter.notifyDataSetChanged()
        })
        return root
    }

    fun init_value(){
        if(model.type != 1){
            tambah.visibility = View.VISIBLE
        }
    }
    fun validasi(view: View){

    }
    fun cek_error(){

    }

    fun hapus(perdagangan: perdagangan){
        perdaganganIni.remove(perdagangan)
        model.pln_bongkar.value = perdaganganIni
        perdaganganAdapter.submitList(perdaganganIni)
        perdaganganAdapter.notifyDataSetChanged()
    }

    fun tambah(perdagangan: perdagangan){
        perdaganganIni.add(perdagangan)
        model.pln_bongkar.value = perdaganganIni
        perdaganganAdapter.submitList(perdaganganIni)
        perdaganganAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && data != null){
            val perdagangan_baru: perdagangan = perdagangan(
                    data.getIntExtra("id", 0),
                    data.getIntExtra("barang_id", 0),
                    data.getDoubleExtra("jml", 0.0),
                    data.getStringExtra("satuan")!!,
                    data.getStringExtra("nama")!!,
                    data.getDoubleExtra("konversi", 0.0),
                    data.getIntExtra("laporan_id", 0)
            )
            tambah(perdagangan_baru)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}