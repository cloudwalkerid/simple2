package id.go.bps.sulbar.simple2.ui.Main

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import id.go.bps.sulbar.simple2.LoginActivity
import id.go.bps.sulbar.simple2.MainActivity
import id.go.bps.sulbar.simple2.MainActivityViewModel
import id.go.bps.sulbar.simple2.R
import id.go.bps.sulbar.simple2.`object`.User
import id.go.bps.sulbar.simple2.adapter.MultiSwipeRefreshLayout


class DashboardFragment : Fragment() {

    private val model: MainActivityViewModel by activityViewModels<MainActivityViewModel>()
    private lateinit var mSwipeRefreshLayout: MultiSwipeRefreshLayout
    private lateinit var holder: LinearLayout
    private lateinit var dashboar_holder: LinearLayout
    private lateinit var nama: TextView
    private lateinit var nama_dashboard: TextView
    private lateinit var nilai_1: TextView
    private lateinit var nilai_2: TextView
    private lateinit var nilai_3: TextView
    private lateinit var nilai_4: TextView
    private lateinit var logout_button: AppCompatButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        viewLifecycleOwner
        val root = inflater.inflate(R.layout.fragment_main_dashboard, container, false)

//        val textView: TextView = root.findViewById(R.id.text_dashboard)
        mSwipeRefreshLayout = root.findViewById(R.id.swiperefresh)
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.primary);

        holder = root.findViewById(R.id.holder)
        dashboar_holder = root.findViewById(R.id.dashboar_holder)
        nama = root.findViewById(R.id.nama)
        nama.visibility = View.GONE
        nama_dashboard = root.findViewById(R.id.nama_dashboard)
        nilai_1 = root.findViewById(R.id.nilai_1);
        nilai_2 = root.findViewById(R.id.nilai_2);
        nilai_3 = root.findViewById(R.id.nilai_3);
        nilai_4 = root.findViewById(R.id.nilai_4);
        logout_button = root.findViewById(R.id.sign_out_button)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeRefreshLayout.setSwipeableChildren(R.id.holder);

        mSwipeRefreshLayout.setOnRefreshListener {
//            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            initiateRefresh()
        }

        logout_button.setOnClickListener {
            model.logout()
        }

        model.user.observe(viewLifecycleOwner, Observer {
            if(model.user.value != null){
                nama.text = model.user.value?.nama
                nama.visibility = View.VISIBLE
            }
        })

        model.processDashboard.observe(viewLifecycleOwner, Observer<Int> { item ->
            // Update the UI
            if(item==1){
                mSwipeRefreshLayout.setRefreshing(true);
                dashboar_holder.visibility = View.GONE
                nama_dashboard.visibility = View.GONE
            }else if(item==2){
                nama_dashboard.text = model.dashboardRepo.waktu.value
                nilai_1.text = model.dashboardRepo.laporan_total.value?.toString()
                nilai_2.text = model.dashboardRepo.laporan_sudah_approve.value?.toString()
                nilai_3.text = model.dashboardRepo.laporan_belum_approve.value?.toString()
                nilai_4.text = model.dashboardRepo.laporan_belum_selesai.value?.toString()
                mSwipeRefreshLayout.setRefreshing(false);
                dashboar_holder.visibility = View.VISIBLE
                nama_dashboard.visibility = View.VISIBLE
            }else if(item==3){
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, model.processDashboardPesan.value, Toast.LENGTH_LONG).show()
                dashboar_holder.visibility = View.GONE
                nama_dashboard.visibility = View.GONE
            }
        })
//        var pertama = true
//        model.user.observe(viewLifecycleOwner, Observer<User> { item ->
//            if(item == null || item.isRefreshExpired() || item.isRefreshTTLExpired()){
//                startActivity(Intent(context, LoginActivity::class.java))
//                activity?.finish()
//            }
//            if(pertama){
//                initiateRefresh()
//            }
//        })
    }
    fun initiateRefresh() {
        model.fetch_dashboard()
    }
}