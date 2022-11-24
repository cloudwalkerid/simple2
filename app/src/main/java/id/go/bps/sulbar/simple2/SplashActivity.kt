package id.go.bps.sulbar.simple2


import android.R.attr.label
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import id.go.bps.sulbar.simple2.`object`.User


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {

    val viewModel: LoginActivityViewModel by viewModels()
    lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        linearLayout = findViewById<LinearLayout>(R.id.board)
        viewModel.user.observe(this) {
            animate(it)
        }
    }
    private fun animate(user: User) {
        linearLayout.animate()
            .alpha(1.0f).setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
//                    label.setVisibility(View.GONE)
//                    loginHolder.setVisibility(View.VISIBLE)
//                    loginHolder.setAlpha(0)
                    linearLayout.animate().alpha(0.0f).setDuration(500)
                }
            })
        val Next: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                } finally {
                    if(user == null || user.isRefreshExpired() || user.isRefreshTTLExpired()){
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
        Next.start()

    }
}