package id.go.bps.sulbar.simple2

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.google.android.material.textfield.TextInputEditText


class LoginActivity : AppCompatActivity(){

    lateinit var username: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var loginButton: Button
    lateinit var errorText: TextView
    lateinit var progressDialog: ProgressDialog

    val viewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById<TextInputEditText>(R.id.username)
        password = findViewById<TextInputEditText>(R.id.password)
        loginButton = findViewById<Button>(R.id.sign_in_button)
        errorText = findViewById<TextView>(R.id.error_message)
        progressDialog = ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Process")


        loginButton.setOnClickListener(View.OnClickListener {
            this.login(username.text.toString(), password.text.toString());
        })

        viewModel.user.observe(this) {
            if(it != null&& (it.isRefreshExpired() || it.isRefreshTTLExpired())){
                val text = "Butuh login ulang\n"+it.nama+" !"
                val duration = Toast.LENGTH_LONG
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()

                errorText.setText(text)
                errorText.setVisibility(View.VISIBLE)
            }else if(it != null && !it.isRefreshExpired() && !it.isRefreshTTLExpired()){
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
        viewModel.processLogin.observe(this){
            if(it == 1){
                errorText.setVisibility(View.GONE)
                progressDialog.setTitle("Login")
                progressDialog.setMessage("Mencoba untuk login ...")
                progressDialog.show()
            }else if (it == 2){
                progressDialog.dismiss()
                val text = "Berhasil login!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }else if (it == 3){
                progressDialog.dismiss()
                val text = "Gagal login!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }
    }
    fun login (userNameString: String, passWordString: String){
        if (userNameString.trim { it <= ' ' }.length == 0 && passWordString.trim { it <= ' ' }.length == 0) {
            errorText.setText("*email dan password harus diisi")
            errorText.setVisibility(View.VISIBLE)
        } else if (userNameString.trim { it <= ' ' }.length == 0) {
            errorText.setText("*email harus diisi")
            errorText.setVisibility(View.VISIBLE)
        } else if (passWordString.trim { it <= ' ' }.length == 0) {
            errorText.setText("*password harus diisi")
            errorText.setVisibility(View.VISIBLE)
        }else{
            viewModel.login(userNameString, passWordString)
        }
    }
}