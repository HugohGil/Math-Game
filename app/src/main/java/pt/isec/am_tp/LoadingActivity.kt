package pt.isec.am_tp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    companion object {
        private const val POINTS_KEY = "points"
        private const val LEVEL_KEY = "level"

        fun getIntent(
            context: Context,
            points: Int,
            level: Int,
        ): Intent {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra(POINTS_KEY, points)
            intent.putExtra(LEVEL_KEY, level)
            return intent
        }
    }
    private lateinit var binding: ActivityLoadingBinding
    private var points: Int = 0
    private var level: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        points = intent.getIntExtra(POINTS_KEY, 0)
        level = intent.getIntExtra(LEVEL_KEY, 0)
        level++
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
                val intent = SinglePlayerActivity.getIntent(context, points, level)
                startActivity(intent)
            }
        }.start()

    }
}
