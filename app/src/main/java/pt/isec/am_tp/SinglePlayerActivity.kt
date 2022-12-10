package pt.isec.am_tp

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivitySingleplayerBinding
import kotlin.Long
import kotlin.getValue
import kotlin.random.Random


class SinglePlayerActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
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
            findViewById<Button>(idAr)
                .text = "${singlePlayerViewModel.operations.get(randomIndexAr)}"
        }

        for(j in 0..8){
            val randomNum = Random.nextInt(1,singlePlayerViewModel.maxNumber)
            val idNum = idsNum.get(j)
            findViewById<Button>(idNum)
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


    //Gesture Detector
    private val gestureDetector : GestureDetector by lazy {
        GestureDetector(this, this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDetector.onTouchEvent(event!!))
            return true
        return super.onTouchEvent(event)
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        Log.i("GESTURE", "onDown: ${p0?.x} / ${p0?.y}")

        return true
    }
    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.i("GESTURE", "onScroll: ${p1?.x} / ${p1?.y}")
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        Log.i("GESTURE", "onShowPress: ")
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        Log.i("GESTURE", "onSingleTapUp: ")
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        Log.i("GESTURE", "onLongPress: ")
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.i("GESTURE", "onFling: ")
        return true
    }

}
