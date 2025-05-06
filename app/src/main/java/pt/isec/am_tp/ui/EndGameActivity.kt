package pt.isec.am_tp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.am_tp.R
import pt.isec.am_tp.data.RetrofitClient
import pt.isec.am_tp.data.model.Score
import pt.isec.am_tp.databinding.ActivityEndGameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        findViewById<TextView>(R.id.txtPointsEndGame)
            .text = "$points"
        findViewById<TextView>(R.id.txtLevelEndGame)
            .text = "$level"
        findViewById<TextView>(R.id.txtTimerEndGame)
            .text = "$totalTime"


        val score = Score(points = points, time = totalTime)

        RetrofitClient.scoreInterface.submitScore(score).enqueue(object : Callback<Score> {
            override fun onResponse(call: Call<Score>, response: Response<Score>) {
                if (response.isSuccessful) {
                    Log.d("API", "Score submitted")
                } else {
                    Log.e("API", "Failed to submit score error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<Score>, t: Throwable) {
                Log.e("API", "Error: ${t.message}", t)
            }
        })

        /*val db = Firebase.firestore           // Firebase code
        val score = hashMapOf(
            "points" to points,
            "time" to totalTime
        )
        db.collection("Score").add(score)*/

    }

}
