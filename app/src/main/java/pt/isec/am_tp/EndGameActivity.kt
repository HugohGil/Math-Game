package pt.isec.am_tp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivityEndGameBinding
import pt.isec.am_tp.databinding.ActivityLoadingBinding

class EndGameActivity : AppCompatActivity() {
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
            val intent = Intent(context, EndGameActivity::class.java)
            intent.putExtra(POINTS_KEY, points)
            intent.putExtra(LEVEL_KEY, level)
            intent.putExtra(TIME_KEY, totalTime)
            return intent
        }
    }
    private lateinit var binding: ActivityEndGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val points = intent.getIntExtra(POINTS_KEY, 0)
        val level = intent.getIntExtra(LEVEL_KEY, 0)
        val totalTime = intent.getIntExtra(TIME_KEY, 0)




    }

}
