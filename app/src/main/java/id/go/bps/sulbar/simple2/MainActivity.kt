package id.go.bps.sulbar.simple2


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import id.go.bps.sulbar.simple2.`object`.User

class MainActivity : AppCompatActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.main_navigation_home, R.id.main_navigation_list_offline, R.id.main_navigation_list_online, R.id.main_navigation_laporan))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar!!.hide()
        var pertama = true
        viewModel.user.observe(this, Observer<User> { item ->
            if(item == null || item.isRefreshExpired() || item.isRefreshTTLExpired()){
                startActivity(Intent(this, LoginActivity::class.java))
                Log.d("Main", "Finish")
                finish()
            }
            if(pertama){
                viewModel.fetch_dashboard()
                viewModel.fetch_dokumen_list()
                viewModel.fetch_laporan()
                pertama = false
                val Next: Thread = object : Thread() {
                    override fun run() {
                        try {
                            sleep(5000)
                        } catch (e: InterruptedException) {
                        } finally {
                            viewModel.refresh()
                        }
                    }
                }
                Next.start()
            }
        })

    }



}