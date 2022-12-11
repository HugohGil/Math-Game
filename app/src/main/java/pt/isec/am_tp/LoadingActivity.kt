package pt.isec.am_tp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.am_tp.databinding.ActivityLoadingBinding
import pt.isec.am_tp.databinding.ActivityTop5Binding

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        startTimer()
    }

    private fun startTimer() {
        var timer = 5
        val context = this
        object : CountDownTimer((timer*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.txtCountDown)
                    .text = "$timer"
                timer--
            }

            override fun onFinish() {
                val intent = Intent(context, SinglePlayerActivity::class.java)
                startActivity(intent)
            }
        }.start()

    }
}
