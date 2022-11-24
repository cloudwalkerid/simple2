package id.go.bps.sulbar.simple2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.*

class PerdaganganBarangAdapter (private val onClickHapus: (perdagangan) -> Unit, private val type: Int)  :
        ListAdapter<perdagangan, PerdaganganBarangAdapter.PerdaganganBarangViewHolder>(PerdaganganDiffCallback) {

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class PerdaganganBarangViewHolder(itemView: View, private val onClickHapus: (perdagangan) -> Unit, private val type: Int) :
            RecyclerView.ViewHolder(itemView) {
        private val barang_nama: TextView = itemView.findViewById(R.id.barang_nama)
        private val jumlah: TextView = itemView.findViewById(R.id.jumlah)
        private val pd_hapus: AppCompatImageButton = itemView.findViewById(R.id.pd_hapus)
        private var currentPd: perdagangan? = null


        init {
            pd_hapus.setOnClickListener {
                currentPd?.let {
                    onClickHapus(it)
                }
            }
        }

        /* Bind flower name and image. */
        fun bind(oneDokumen: perdagangan) {
            currentPd = oneDokumen
            barang_nama.text = oneDokumen.nama
            jumlah.text = oneDokumen.jml.toFloat().toString()+" "+oneDokumen.satuan+" ("+oneDokumen.konversi.toFloat().toString()+" Ton)"
            if(type == 1){
                pd_hapus.visibility = View.GONE
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerdaganganBarangViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pd, parent, false)
        return PerdaganganBarangViewHolder(view, onClickHapus, type)
    }

    override fun onBindViewHolder(holder: PerdaganganBarangViewHolder, position: Int) {
        val flower = getItem(position)
        holder.bind(flower)
    }
}

object PerdaganganDiffCallback : DiffUtil.ItemCallback<perdagangan>() {
    override fun areItemsTheSame(oldItem: perdagangan, newItem: perdagangan): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: perdagangan, newItem: perdagangan): Boolean {
        return oldItem.id == newItem.id
    }
}