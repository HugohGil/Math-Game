package pt.isec.am_tp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.am_tp.databinding.ActivityTop5Binding

class Top5Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTop5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTop5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Firebase.firestore
        val ids = arrayOf(R.id.txtTop1, R.id.txtTop2, R.id.txtTop3, R.id.txtTop4, R.id.txtTop5)
        var i = 0

        db.collection("Score")
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
            }
    }

}
