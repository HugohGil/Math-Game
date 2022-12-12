package pt.isec.am_tp

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    companion object {
        private const val POINTS_KEY = "points"
        private const val LEVEL_KEY = "level"
        private const val TIME_KEY = "totalTime"

        fun getIntent(
            context: Context,
            points: Int,
            level: Int,
            totalTime: Int,
        ): Intent {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra(POINTS_KEY, points)
            intent.putExtra(LEVEL_KEY, level)
            intent.putExtra(TIME_KEY, totalTime)
            return intent
        }
    }
    private lateinit var binding: ActivityLoadingBinding
    private var points: Int = 0
    private var level: Int = 0
    var countDownTimer: CountDownTimer? = null
    var timer = 5
    var totalTime = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        points = intent.getIntExtra(POINTS_KEY, 0)
        level = intent.getIntExtra(LEVEL_KEY, 0)
        totalTime = intent.getIntExtra(TIME_KEY, 0)
        level++
        var isPaused = false
        findViewById<TextView>(R.id.txtPoints)
            .text = "$points"
        findViewById<TextView>(R.id.txtDifficulty)
            .text = "$level"

        when(level){
            2 -> {
                findViewById<TextView>(R.id.txtTime)
                    .text = "60"
                findViewById<TextView>(R.id.txtExpression)
                    .text = "10"
                findViewById<TextView>(R.id.txtValue)
                    .text = "1 - 19"
                findViewById<TextView>(R.id.txtOperator)
                    .text = "+ - x ÷"
                findViewById<TextView>(R.id.txtBonus)
                    .text = "5"
            }
            3-> {
                findViewById<TextView>(R.id.txtTime)
                    .text = "30"
                findViewById<TextView>(R.id.txtExpression)
                    .text = "15"
                findViewById<TextView>(R.id.txtValue)
                    .text = "1 - 99"
                findViewById<TextView>(R.id.txtOperator)
                    .text = "+ - x ÷"
                findViewById<TextView>(R.id.txtBonus)
                    .text = "10"
            }
        }

        startTimer()
        binding.btnPause?.setOnClickListener {
            isPaused = if(!isPaused){
                countDownTimer?.cancel()
                true
            } else{
                startTimer()
                false
            }
        }
    }

    private fun startTimer() {
        val context = this
        countDownTimer = object : CountDownTimer((timer*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.txtCountDown)
                    .text = "$timer"
                timer--
            }

            override fun onFinish() {
                val intent = SinglePlayerActivity.getIntent(context, points, level, totalTime)
                startActivity(intent)
            }
        }.start()

    }
}
