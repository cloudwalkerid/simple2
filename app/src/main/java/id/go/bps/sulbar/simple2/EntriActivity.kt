package id.go.bps.sulbar.simple2

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class EntriActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    val viewModel: EntriActivityViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entri)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
//        supportActionBar!!.hide()


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.blok_1, R.id.blok_2, R.id.blok_3_a, R.id.blok_3_b, R.id.blok_4_a, R.id.blok_4_b, R.id.blok_5
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        progressDialog = ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Mendapatkan data")

        val type = intent.getIntExtra("TYPE", 0) // 0: tambah 1: lihat online 2: Edit offline 3: Edit online
        viewModel.type = type
        viewModel.id = intent.getIntExtra("ID", -1)
        viewModel.uuid = intent.getStringExtra("UUID")
//        Toast.makeText(this, "load "+type, Toast.LENGTH_LONG).show()
        viewModel.user.observe(this){
            if(type == 0){
                viewModel.process_load.value = 1
                viewModel.process_load.value = 2
            }else if(type == 1){
                viewModel.fetch_dokumen_online()
            }else if(type == 2){
                viewModel.fetch_dokumen_offline()
            }else if(type == 3){
                viewModel.fetch_dokumen_online()
            }
        }
        viewModel.process_load.observe(this){
            if(it == 1){
                progressDialog.setTitle("Proses")
                progressDialog.setMessage("Mencoba Mendapatkan Data ...")
                progressDialog.show()
            }else if (it == 2){
                progressDialog.dismiss()
                val text = "Berhasil memperoses data!"
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(applicationContext, text, duration).show()
            }else if (it == 3){
                progressDialog.dismiss()
                val text = "Gagal mendapatkan data!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.entri, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}