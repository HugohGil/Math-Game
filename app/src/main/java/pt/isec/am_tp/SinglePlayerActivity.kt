package pt.isec.am_tp

import android.R.attr.button
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivitySingleplayerBinding
import java.lang.Math.abs
import kotlin.random.Random


class SinglePlayerActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    companion object{
        const val MIN_DISTANCE = 100
    }


    val singlePlayerViewModel : SinglePlayerViewModel by viewModels()
    private lateinit var binding: ActivitySingleplayerBinding
    private var buttons: MutableList<Button> = ArrayList()
    var x1:Float = 0.0f
    var y1:Float = 0.0f
    var x2:Float = 0.0f
    var y2:Float = 0.0f
    var height:Float = 0.0f
    var width:Float = 0.0f

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

        /*for (e in buttons) {      // test
            println(e.text)
        }*/
    }

    private fun generateBoard() {

        val ids = arrayOf(R.id.btnNum0, R.id.btnAr0, R.id.btnNum1, R.id.btnAr1, R.id.btnNum2, R.id.btnAr2, R.id.btnAr3,
            R.id.btnAr4, R.id.btnNum3, R.id.btnAr5, R.id.btnNum4, R.id.btnAr6, R.id.btnNum5, R.id.btnAr7, R.id.btnAr8,
            R.id.btnAr9, R.id.btnNum6, R.id.btnAr10, R.id.btnNum7, R.id.btnAr11, R.id.btnNum8)

        for(i in 0..20){
            if(i in 5..7 || i in 13..15){       // rows with only arithmetic buttons
                val randomIndexAr = Random.nextInt(singlePlayerViewModel.operations.size)
                val idAr = ids.get(i)
                findViewById<Button>(idAr)
                    .text = "${singlePlayerViewModel.operations.get(randomIndexAr)}"
                buttons.add(findViewById(idAr));
                continue;
            }
            if(i%2 == 0){
                val randomNum = Random.nextInt(1,singlePlayerViewModel.maxNumber)
                val idNum = ids.get(i)
                findViewById<Button>(idNum)
                    .text = "${randomNum}"
                buttons.add(findViewById(idNum));
            }
            else{
                val randomIndexAr = Random.nextInt(singlePlayerViewModel.operations.size)
                val idAr = ids.get(i)
                findViewById<TextView>(idAr)
                    .text = "${singlePlayerViewModel.operations.get(randomIndexAr)}"
                buttons.add(findViewById(idAr));
            }
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
        gestureDetector.onTouchEvent(event!!)


        if(event.action == 0){          // start
            x1 = event.x
            y1 = event.y
        }
        else if(event.action == 1){     // end
            x2 = event.x
            y2 = event.y

            val distanceX:Float = x2 - x1
            val distanceY:Float = y2 - y1

            if(abs(distanceX) > MIN_DISTANCE && abs(distanceY) > MIN_DISTANCE)      // no diagonal
                return false
            if(abs(distanceX) > MIN_DISTANCE){
                if(x2 > x1){
                    Toast.makeText(this, "Righ swipe", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Left swipe", Toast.LENGTH_LONG).show()
                }
                var button : Button? = checkButton()
                if(button != null){
                }
            }
            else if(abs(distanceY) > MIN_DISTANCE){
                if(y2 > y1){
                    Toast.makeText(this, "Bottom swipe", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Top swipe", Toast.LENGTH_LONG).show()
                }
                checkButton()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun checkButton(): Button? {
        for (b in buttons) {
            width = b.width.toFloat()
            height = b.height.toFloat()
            val point: Point = getPointOfView(b)

            val x1Button = point.x
            val y1Button = point.y
            val x2Button:Float = x1Button + width
            val y2Button:Float = y1Button + height
            if(x1 > x1Button && x1 < x2Button && y1 > y1Button && y1 < y2Button){
                System.out.println(b.text)
                return b
            }
        }
        return null
    }

    private fun getPointOfView(view: View): Point {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return Point(location[0], location[1])
    }

    override fun onDown(p0: MotionEvent?): Boolean {
       // Log.i("GESTURE", "onDown: ${p0?.x} / ${p0?.y}")

        return true
    }
    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
       // Log.i("GESTURE", "onScroll: ${p1?.x} / ${p1?.y}")
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        //Log.i("GESTURE", "onShowPress: ")
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        //Log.i("GESTURE", "onSingleTapUp: ")
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        //Log.i("GESTURE", "onLongPress: ")
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        //Log.i("GESTURE", "onFling: ")
        return true
    }

}
