package pt.isec.am_tp

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivitySingleplayerBinding
import java.lang.Math.abs
import kotlin.random.Random


class SinglePlayerActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    companion object{
        const val MIN_DISTANCE = 100
        private const val POINTS_KEY = "points"
        private const val LEVEL_KEY = "level"
        private const val TIME_KEY = "totalTime"

        fun getIntent(
            context: Context,
            points: Int,
            level: Int,
            totalTime: Int,
        ): Intent {
            val intent = Intent(context, SinglePlayerActivity::class.java)
            intent.putExtra(POINTS_KEY, points)
            intent.putExtra(LEVEL_KEY, level)
            intent.putExtra(TIME_KEY, totalTime)
            return intent
        }
    }

    val singlePlayerViewModel : SinglePlayerViewModel by viewModels()
    private lateinit var binding: ActivitySingleplayerBinding
    private lateinit var buttonsArray: Array<Array<Button?>>
    private lateinit var rowEquations: Array<Array<Button?>>
    private lateinit var colEquations: Array<Array<Button?>>
    private lateinit var rowEquationsSolution: FloatArray
    private lateinit var colEquationsSolution: FloatArray

    private var x1:Float = 0.0f
    private var y1:Float = 0.0f
    private var x2:Float = 0.0f
    private var y2:Float = 0.0f
    private var height:Float = 0.0f
    private var width:Float = 0.0f
    var countDownTimer: CountDownTimer? = null
    private var timer:Int = 90
    private var totalTime:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val points = intent.getIntExtra(POINTS_KEY, 0)
        val level = intent.getIntExtra(LEVEL_KEY, 1)
        totalTime = intent.getIntExtra(TIME_KEY, 0)
        singlePlayerViewModel.points = points
        singlePlayerViewModel.level = level
        generateLevel()
        generateBoard()
        startTimer()
    }

    private fun generateLevel() {
        singlePlayerViewModel.operations.add("+")
        singlePlayerViewModel.operations.add("-")
        when(singlePlayerViewModel.level){
            2 -> {
                timer = 60
                singlePlayerViewModel.timerLimit = 60
                singlePlayerViewModel.maxNumber = 20
                singlePlayerViewModel.expressions = 2
                singlePlayerViewModel.operations.add("x")
                singlePlayerViewModel.operations.add("÷")
                singlePlayerViewModel.bonus = 5
            }
            3-> {
                timer = 30
                singlePlayerViewModel.timerLimit = 30
                singlePlayerViewModel.maxNumber = 100
                singlePlayerViewModel.expressions = 2
                singlePlayerViewModel.operations.add("x")
                singlePlayerViewModel.operations.add("÷")
                singlePlayerViewModel.bonus = 10
            }
        }
        findViewById<TextView>(R.id.txtDifficulty)
            .text = "${singlePlayerViewModel.level}"
        findViewById<TextView>(R.id.txtExpression)
            .text = "${singlePlayerViewModel.expressions}"
        findViewById<TextView>(R.id.txtPoints)
            .text = "${singlePlayerViewModel.points}"
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer((timer*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.txtTimer)
                    .text = "$timer"
                timer--
                totalTime++
            }

            override fun onFinish() {
                endGame()
            }
        }.start()
    }

    private fun generateBoard() {
        /*
        Generates game board
         */
        val ids = arrayOf(R.id.btnNum0, R.id.btnAr0, R.id.btnNum1, R.id.btnAr1, R.id.btnNum2,
            R.id.btnAr2, R.id.btnSpace11, R.id.btnAr3, R.id.btnSpace13, R.id.btnAr4,
            R.id.btnNum3, R.id.btnAr5, R.id.btnNum4, R.id.btnAr6, R.id.btnNum5,
            R.id.btnAr7, R.id.btnSpace31, R.id.btnAr8, R.id.btnSpace33, R.id.btnAr9,
            R.id.btnNum6, R.id.btnAr10, R.id.btnNum7, R.id.btnAr11, R.id.btnNum8)

        buttonsArray = Array(5) { Array(5) { null } } //initialize buttonsArray with null elements
        for(row in 0..4){
            for(col in 0..4){
                if(row % 2 == 0 && col % 2 == 0){ //se btnNum
                    val randomNum = Random.nextInt(1,singlePlayerViewModel.maxNumber)
                    findViewById<TextView>(ids[row * 5 + col]).text = "$randomNum"
                    buttonsArray[row][col] = findViewById(ids[row * 5 + col])
                }
                if((row % 2 != 0 && col % 2 == 0) || (row % 2 == 0 && col % 2 != 0)){ //se btnAr
                    val randomIndexAr = Random.nextInt(singlePlayerViewModel.operations.size)
                    findViewById<TextView>(ids[row * 5 + col])
                        .text = singlePlayerViewModel.operations[randomIndexAr]
                    buttonsArray[row][col] = findViewById(ids[row * 5 + col])
                }
            }
        }
        rowEquations = Array(3) { Array(5) { null } }
        var count = 0
        for(i in 0..2) {
            for (row in 0..4) {
                for (col in 0..4) {
                    if (row == count) { //se btnNum
                        rowEquations[i][col] = buttonsArray[row][col]
                    }
                }
            }
            count += 2
        }
        colEquations= Array(3) { Array(5) { null } }
        count = 0
        for(i in 0..2) {
            for (col in 0..4) {
                for (row in 0..4) {
                    if (col == count) { //se btnNum
                        colEquations[i][row] = buttonsArray[row][col]
                    }
                }
            }
            count += 2
        }
        rowEquationsSolution = FloatArray(3)
        for(i in rowEquations.indices){
            rowEquationsSolution[i] = solveEquation(rowEquations[i])
        }
        colEquationsSolution = FloatArray(3)
        for(i in colEquations.indices){
            colEquationsSolution[i] = solveEquation(colEquations[i])
        }
    }

    private fun solveEquation(equation : Array<Button?>) : Float{
        /*
        Receives array of buttons and solves the equation present on their .texts property
        Returns result of equation
         */
        var string = ""
        for(i in equation) {
            val aux = i?.text
            string += (aux.toString() + "|")
        }
        var parts: ArrayList<String> = ArrayList(string.split("|"))
        parts.removeLast() //String.split creates a unnecessary token
        Log.i("Solve Equation", parts.toString())
        while(parts.size != 1) {
            for (i in 0 until parts.size) { //calculates first and/or second operation if they are multiplication or division
                if (!parts.contains("x") && !parts.contains("÷"))
                    break
                if (i % 2 == 1) {
                    while (parts[i] == "x" || parts[i] == "÷") {
                        val a = parts[i - 1]
                        val b = parts[i]
                        val c = parts[i + 1]
                        parts.removeAt(i+1)
                        parts.removeAt(i)
                        parts.removeAt(i-1)
                        val d = solveArithmetic(a.toFloat(), b, c.toFloat()).toString()
                        parts.add(i - 1, d)
                        if (parts.size == 1 || (!parts.contains("x") && !parts.contains("÷")))
                            break
                    }
                }
            }
            for (i in 0 until parts.size) { //calculates first and/or second operation if they are sum or subtraction
                if (!parts.contains("+") && !parts.contains("-"))
                    break
                if (i % 2 == 1) {
                    while (parts[i] == "+" || parts[i] == "-") {
                        val a = parts[i - 1]
                        val b = parts[i]
                        val c = parts[i + 1]
                        parts.removeAt(i + 1)
                        parts.removeAt(i)
                        parts.removeAt(i - 1)
                        val d = solveArithmetic(a.toFloat(), b, c.toFloat()).toString()
                        parts.add(i - 1, d)
                        if (parts.size == 1 || (!parts.contains("+") && !parts.contains("-")))
                            break
                    }
                }
            }
        }
        return parts[0].toFloat()
    }

    private fun solveArithmetic(a:Float, operation:String, b:Float) : Float{
        /*
        Solves an arithmetic equation between a and b using "operation" operation
        Returns the result
         */
        var result = 0.0f
        when(operation){
            "x"-> result = a*b
            "÷"-> result = a/b
            "+"-> result = a+b
            "-"-> result = a-b
        }
        return result
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
            var points = 0

            if(abs(distanceX) > MIN_DISTANCE && abs(distanceY) > MIN_DISTANCE)      // no diagonal
                return false
            if(abs(distanceX) > MIN_DISTANCE){
                Log.i("Swipe", "Horizontal Swipe")
                val button: Button? = checkButton()
                if(button != null){
                    points = checkGuess(rowEquationsSolution[checkRow(button)])
                    singlePlayerViewModel.points += points
                    findViewById<TextView>(R.id.txtPoints)
                        .text = "${singlePlayerViewModel.points}"
                    generateBoard()
                }
            }
            else if(abs(distanceY) > MIN_DISTANCE){
                Log.i("Swipe", "Vertical Swipe")
                val button: Button? = checkButton()
                if(button != null){
                    points = checkGuess(colEquationsSolution[checkColumn(button)])
                    singlePlayerViewModel.points += points
                    findViewById<TextView>(R.id.txtPoints)
                        .text = "${singlePlayerViewModel.points}"
                    generateBoard()
                }
            }
            if(points == 2) { // right option
                correctExpression()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun correctExpression() {
        singlePlayerViewModel.expressions--
        findViewById<TextView>(R.id.txtExpression)
            .text = "${singlePlayerViewModel.expressions}"
        countDownTimer?.cancel()
        timer += singlePlayerViewModel.bonus
        if(timer > singlePlayerViewModel.timerLimit)
            timer = singlePlayerViewModel.timerLimit
        startTimer()
        if (singlePlayerViewModel.expressions == 0) {    // next level
            endGame()
            countDownTimer?.cancel()
            val intent = LoadingActivity.getIntent(
                this,
                singlePlayerViewModel.points,
                singlePlayerViewModel.level,
                totalTime
            )
            startActivity(intent)
        }
    }

    private fun endGame() {
        val intent = LoadingActivity.getIntent(
            this,
            singlePlayerViewModel.points,
            singlePlayerViewModel.level,
            totalTime
        )
        startActivity(intent)
    }

    private fun checkGuess(result: Float) : Int{
        /*
        Verifies and grades user guess
        Returns
            2 -> max solution guessed
            1 -> 2nd max solution guessed
            0 -> failed guessing
         */
        val temp : ArrayList<Float> = ArrayList(colEquationsSolution.toList())
        temp.addAll(rowEquationsSolution.toList())
        for(i in temp)
            Log.i("SOLUTION TEMP", "$i")
        val solutionArray : ArrayList<Float> = ArrayList()
        for(i in temp){
            if(!solutionArray.contains(i)){
                solutionArray.add(i)
            }
        }
        solutionArray.sort()
        solutionArray.reverse()
        for(i in solutionArray)
            Log.i("SOLUTION LIST", "$i")
        Log.i("SOLUTION PICKED", "$result")
        return when (result) {
            solutionArray[0] -> 2
            solutionArray[1] -> 1
            else -> 0
        }
    }
    private fun checkRow(button: Button) : Int{
        /*
        Checks which row button belongs to
        Returns the Index of the row in the rowEquations array
         */
        for(row in 0..4){
            for(col in 0..4){
                if (buttonsArray[row][col] == button){
                    return row/2
                }
            }
        }
        return -1
    }
    private fun checkColumn(button: Button) : Int {
        /*
        Checks which column button belongs to
        Returns the Index of the column in the colEquations array
         */
        for(row in 0..4){
            for(col in 0..4){
                if (buttonsArray[row][col] == button){
                    return col/2
                }
            }
        }
        return -1
    }

    private fun checkButton() : Button?{
        /*
            Detects if swipe started on button and if it did, returns button reference
         */
        for(row in buttonsArray){
            for (b in row) {
                if(b != null){
                    width = b.width.toFloat()
                    height = b.height.toFloat()
                    val point: Point = getPointOfView(b)

                    val x1Button = point.x
                    val y1Button = point.y
                    val x2Button:Float = x1Button + width
                    val y2Button:Float = y1Button + height
                    if(x1 > x1Button && x1 < x2Button && y1 > y1Button && y1 < y2Button){
                        Log.i("SwipeOnButton", "${b.text}")
                        return b
                    }
                }
            }
        }
        return null
    }

    private fun getPointOfView(view: View): Point {
        /*
        Returns Point of a Button's coordinates
         */
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return Point(location[0], location[1])
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.msg_title_backout)
            .setMessage(R.string.msg_backout)
            .setPositiveButton(R.string.msg_confirm) { _, _ ->
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton(R.string.msg_deny, null)
            .show()
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
