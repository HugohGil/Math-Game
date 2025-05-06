package pt.isec.am_tp.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.R
import pt.isec.am_tp.data.RetrofitClient
import pt.isec.am_tp.data.model.Score
import pt.isec.am_tp.databinding.ActivityTop5Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Top5Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTop5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTop5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //val db = Firebase.firestore
        val ids = arrayOf(R.id.txtTop1, R.id.txtTop2, R.id.txtTop3, R.id.txtTop4, R.id.txtTop5)
        var i = 0
        RetrofitClient.scoreInterface.getLeaderboard().enqueue(object : Callback<List<Score>> {
            override fun onResponse(call: Call<List<Score>>, response: Response<List<Score>>) {
                if (response.isSuccessful) {
                    val top5List = response.body()
                    if (top5List != null) {
                        for (score in top5List) {
                            println(score.points)
                            val display = "${getString(R.string.msg_score)} ${score.points}" +
                                    " ${getString(R.string.msg_time)} ${score.time}"
                            findViewById<TextView>(ids[i])
                                .text = display
                            i++
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Score>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })

        /*db.collection("Score")                                    // Firebase code
            .orderBy("points", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(i == 5)
                        break
                    val display = "${getString(R.string.msg_score)} ${document.data["points"]}" +
                            " ${getString(R.string.msg_time)} ${document.data["time"]}"
                    Log.d(TAG, "${document.id} => ${document.data}")
                    findViewById<TextView>(ids[i])
                        .text = display
                    i++
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }*/
    }

}
