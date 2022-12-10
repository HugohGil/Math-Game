package pt.isec.am_tp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.am_tp.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var appLanguage = "en"
    private var currentLanguage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appLanguage = intent.getStringExtra(currentLanguage).toString()
        if (appLanguage == "null") {                            // first time
            setDefaultLanguage()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, GameModeActivity::class.java)
            startActivity(intent)
        }

        binding.btnTop5.setOnClickListener {
            val db = Firebase.firestore                     //TODO this will be moved to when player completes game
            val score = hashMapOf(
            "points" to 40,
            "time" to 20
            )
            db.collection("Score").add(score)

            val intent = Intent(this, Top5Activity::class.java)
            startActivity(intent)
        }

        binding.btnLanguage.setOnClickListener {
            changeLanguage()
        }

        binding.btnCredits.setOnClickListener {
            val intent = Intent(this, CreditsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setDefaultLanguage() {
        if (Locale.getDefault().language == "pt")
            setLocale("pt")
        else
            setLocale("en")
    }

    private fun changeLanguage() {
        val languages = arrayOf("English", "Português")

        val dlgLanguage = AlertDialog.Builder(this)
            .setTitle(R.string.msg_choose_language)
            .setSingleChoiceItems(languages, -1) { _, which ->
                if (which == 0) {
                    setLocale("en")
                } else if (which == 1) {
                    setLocale("pt")
                }
            }
            .setNegativeButton(R.string.msg_cancel, null)
            .create()
        dlgLanguage.show()
    }

    private fun setLocale(l: String) {
        if (appLanguage != l) {
            val locale = Locale(l)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)

            val intent = Intent(
                this,
                MainActivity::class.java
            )
            intent.putExtra(currentLanguage, l)
            startActivity(intent)
        } else
            Toast.makeText(this, R.string.msg_error_language, Toast.LENGTH_LONG).show()
    }
}