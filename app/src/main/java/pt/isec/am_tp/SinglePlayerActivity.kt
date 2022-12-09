package pt.isec.am_tp

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivitySingleplayerBinding
import kotlin.Long
import kotlin.getValue


class SinglePlayerActivity : AppCompatActivity() {
    val timerModel : TimerViewModel by viewModels()
    private lateinit var binding: ActivitySingleplayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViewById<TextView>(R.id.txtTimer)
            .text = "${timerModel.timer}"
        startTimer()
    }

    private fun startTimer() {
        object : CountDownTimer((timerModel.timer*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.txtTimer)
                    .text = "${timerModel.timer}"
                timerModel.timer--
            }

            override fun onFinish() {
                findViewById<TextView>(R.id.txtTimer)
                    .text = "bingbong"
            }
        }.start()
    }

}
