package id.go.bps.sulbar.simple2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.*
import java.util.*


class DataOfflineAdapter(private val onClickEdit: (Dokumen) -> Unit, private val onClickHapus: (Dokumen) -> Unit
                         , private val onClickKirim: (Dokumen) -> Unit, private val getProvs: () -> List<Prov>, private val getKabs: () -> List<Kab>
                         , private val getKantorUnits: () -> List<Kantor_Unit>, private val getPelabuhans: () -> List<Pelabuhan>) :
        ListAdapter<Dokumen, DataOfflineAdapter.DataOfflineViewHolder>(DokumenDiffCallbackOn) {

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class DataOfflineViewHolder(itemView: View, val onClickEdit: (Dokumen) -> Unit, val onClickHapus: (Dokumen) -> Unit, val onClickKirim: (Dokumen) -> Unit
                                , private val getProvs: () -> List<Prov>, private val getKabs: () -> List<Kab>
                                , private val getKantorUnits: () -> List<Kantor_Unit>, private val getPelabuhans: () -> List<Pelabuhan>) :
            RecyclerView.ViewHolder(itemView) {
        private val entri_wilayah: TextView = itemView.findViewById(R.id.entri_wilayah)
        private val entri_pelabuhan: TextView = itemView.findViewById(R.id.entri_pelabuhan)
        private val entri_nama: TextView = itemView.findViewById(R.id.entri_nama)
        private val entri_berangkat: TextView = itemView.findViewById(R.id.entri_berangkat)
        private val button_holder: LinearLayout = itemView.findViewById(R.id.button_holder)
        private val entri_edit: AppCompatButton = itemView.findViewById(R.id.entri_edit)
        private val entri_hapus: AppCompatButton = itemView.findViewById(R.id.entri_hapus)
        private val entri_kirim: AppCompatButton = itemView.findViewById(R.id.entri_kirim)
        private val entri_last_edited: TextView = itemView.findViewById(R.id.entri_last_edited)
        private var currentDokumen: Dokumen? = null


        init {
            entri_edit.setOnClickListener {
                currentDokumen?.let {
                    onClickEdit(it)
                }
            }
            entri_hapus.setOnClickListener {
                currentDokumen?.let {
                    onClickHapus(it)
                }
            }
            entri_kirim.setOnClickListener {
                currentDokumen?.let {
                    onClickKirim(it)
                }
            }
        }

        /* Bind flower name and image. */
        fun bind(oneDokumen: Dokumen) {
            currentDokumen = oneDokumen
            entri_wilayah.text = oneDokumen.getWilayahKabProv(getProvs(), getKabs())
            entri_pelabuhan.text = oneDokumen.getWilayahPelabuhan(getPelabuhans())
            entri_nama.text = oneDokumen.nama_kapal_1+" "+oneDokumen.nama_kapal
            entri_berangkat.text = oneDokumen.getBerangkat()
            if(oneDokumen.jml_error == 0){
                entri_kirim.visibility = View.VISIBLE
                button_holder.weightSum = 3f
            }else{
                entri_kirim.visibility = View.GONE
                button_holder.weightSum = 2f
            }

            val cal = Calendar.getInstance()
            cal.time = Date(oneDokumen.lastEdited)
            val tanggal: String = ""+cal[Calendar.DAY_OF_MONTH]+"-"+(cal[Calendar.MONTH]+1)+"-"+(cal[Calendar.YEAR])
            val jam: String = ""+cal[Calendar.HOUR_OF_DAY]+":"+cal[Calendar.MINUTE]

            entri_last_edited.text = "Terakhir edit, "+tanggal+" "+jam+" "

//            flowerTextView.text = flower.name
//            if (flower.image != null) {
//                flowerImageView.setImageResource(flower.image)
//            } else {
//                flowerImageView.setImageResource(R.drawable.rose)
//            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataOfflineViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.entri_item_offline, parent, false)
        return DataOfflineViewHolder(view, onClickEdit, onClickHapus, onClickKirim, getProvs, getKabs
                , getKantorUnits, getPelabuhans)
    }

    override fun onBindViewHolder(holder: DataOfflineViewHolder, position: Int) {
        val flower = getItem(position)
        holder.bind(flower)
    }
}

object DokumenDiffCallbackOn : DiffUtil.ItemCallback<Dokumen>() {
    override fun areItemsTheSame(oldItem: Dokumen, newItem: Dokumen): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Dokumen, newItem: Dokumen): Boolean {
        return oldItem.uuid == newItem.uuid
    }
}