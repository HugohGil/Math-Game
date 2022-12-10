package pt.isec.am_tp

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivitySingleplayerBinding
import kotlin.Long
import kotlin.getValue
import kotlin.random.Random


class SinglePlayerActivity : AppCompatActivity() {
    val singlePlayerViewModel : SinglePlayerViewModel by viewModels()
    private lateinit var binding: ActivitySingleplayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViewById<TextView>(R.id.txtTimer)
            .text = "${singlePlayerViewModel.timer}"

        singlePlayerViewModel.operations.add("+")
        singlePlayerViewModel.operations.add("-")

        generateBoard()
        startTimer()
    }

    private fun generateBoard() {
        val idsAr = arrayOf(R.id.btnAr0, R.id.btnAr1,R.id.btnAr2,R.id.btnAr3,R.id.btnAr4,R.id.btnAr5,
            R.id.btnAr6,R.id.btnAr7,R.id.btnAr8,R.id.btnAr9,R.id.btnAr10,R.id.btnAr11)
        val idsNum = arrayOf(R.id.btnNum0, R.id.btnNum1,R.id.btnNum2,R.id.btnNum3,R.id.btnNum4,R.id.btnNum5,
            R.id.btnNum6,R.id.btnNum7,R.id.btnNum8)



        for(i in 0..11){
            val randomIndexAr = Random.nextInt(singlePlayerViewModel.operations.size)
            val idAr = idsAr.get(i)
            findViewById<TextView>(idAr)
                .text = "${singlePlayerViewModel.operations.get(randomIndexAr)}"
        }

        for(j in 0..8){
            val randomNum = Random.nextInt(1,singlePlayerViewModel.maxNumber)
            val idNum = idsNum.get(j)
            findViewById<TextView>(idNum)
                .text = "${randomNum}"
        }


    }

    private fun startTimer() {
        object : CountDownTimer((singlePlayerViewModel.timer*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.txtTimer)
                    .text = "${singlePlayerViewModel.timer}"
                singlePlayerViewModel.timer--
            }

            override fun onFinish() {
                findViewById<TextView>(R.id.txtTimer)
                    .text = "bingbong"
            }
        }.start()
    }

}
