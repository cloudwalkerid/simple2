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


class DataOnlineAdapter(private val onClickEdit: (Dokumen) -> Unit, private val onClickHapus: (Dokumen) -> Unit
                        , private val onClickLihat: (Dokumen) -> Unit, private val onClickApprove: (Dokumen) -> Unit, private val getProvs: () -> List<Prov>, private val getKabs: () -> List<Kab>
                        , private val getKantorUnits: () -> List<Kantor_Unit>, private val getPelabuhans: () -> List<Pelabuhan>
                        , private val getOfflineDokumen: () -> List<Dokumen>, private val theuser: User) :
        ListAdapter<Dokumen, DataOnlineAdapter.DataOnlineViewHolder>(DokumenDiffCallback) {

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class DataOnlineViewHolder(itemView: View, val onClickEdit: (Dokumen) -> Unit, val onClickHapus: (Dokumen) -> Unit
                                , private val onClickLihat: (Dokumen) -> Unit, private val onClickApprove: (Dokumen) -> Unit
                                , private val getProvs: () -> List<Prov>, private val getKabs: () -> List<Kab>
                                , private val getKantorUnits: () -> List<Kantor_Unit>, private val getPelabuhans: () -> List<Pelabuhan>
                                , private val getOfflineDokumen: () -> List<Dokumen>, private val user: User) :
            RecyclerView.ViewHolder(itemView) {
        private val entri_wilayah: TextView = itemView.findViewById(R.id.entri_wilayah)
        private val entri_pelabuhan: TextView = itemView.findViewById(R.id.entri_pelabuhan)
        private val entri_nama: TextView = itemView.findViewById(R.id.entri_nama)
        private val entri_berangkat: TextView = itemView.findViewById(R.id.entri_berangkat)
        private val button_holder: LinearLayout = itemView.findViewById(R.id.button_holder)
        private val entri_edit: AppCompatButton = itemView.findViewById(R.id.entri_edit)
        private val entri_hapus: AppCompatButton = itemView.findViewById(R.id.entri_hapus)
        private val entri_detil: AppCompatButton = itemView.findViewById(R.id.entri_detil)
        private val entri_approve: AppCompatButton = itemView.findViewById(R.id.entri_approve)
        private val entri_status: TextView = itemView.findViewById(R.id.entri_status)
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
            entri_detil.setOnClickListener {
                currentDokumen?.let {
                    onClickLihat(it)
                }
            }
            entri_approve.setOnClickListener{
                currentDokumen?.let {
                    onClickApprove(it)
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

            var jumlah_weight = 1 // lihat selalu ada
            if(oneDokumen.approval == 0){
                jumlah_weight = jumlah_weight + 2
                entri_edit.visibility = View.VISIBLE
                entri_hapus.visibility = View.VISIBLE
            }else{
                entri_edit.visibility = View.GONE
                entri_hapus.visibility = View.GONE
            }
            if(oneDokumen.approval == 0 && oneDokumen.status == 1 && user.leveluser_id<=5){
                jumlah_weight = jumlah_weight + 1
                entri_approve.visibility = View.VISIBLE
            }else{
                entri_approve.visibility = View.GONE
            }
            button_holder.weightSum = jumlah_weight.toFloat()
            if(getOfflineDokumen() != null){
                val dokumenSatu = getOfflineDokumen().find{oneDokumen.id == it.id}
                if(dokumenSatu == null){
                    button_holder.visibility = View.VISIBLE
                    entri_status.visibility = View.GONE
                }else{
                    button_holder.visibility = View.GONE
                    entri_status.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataOnlineViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.entri_item_onine, parent, false)
        return DataOnlineViewHolder(view, onClickEdit, onClickHapus, onClickLihat, onClickApprove, getProvs, getKabs
                , getKantorUnits, getPelabuhans, getOfflineDokumen, theuser)
    }

    override fun onBindViewHolder(holder: DataOnlineViewHolder, position: Int) {
        val flower = getItem(position)
        holder.bind(flower)
    }
}

object DokumenDiffCallback : DiffUtil.ItemCallback<Dokumen>() {
    override fun areItemsTheSame(oldItem: Dokumen, newItem: Dokumen): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Dokumen, newItem: Dokumen): Boolean {
        return oldItem.uuid == newItem.uuid
    }
}