package pt.isec.am_tp


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.am_tp.databinding.ActivityMenuBinding
import java.util.*


class MenuActivity : AppCompatActivity() {
    companion object {
        private const val LANGUAGE_KEY = "language"

        fun getIntent(
            context: Context,
            language: String,
        ): Intent {
            val intent = Intent(context, MenuActivity::class.java)
            intent.putExtra(LANGUAGE_KEY, language)
            return intent
        }
    }
    private lateinit var binding: ActivityMenuBinding
    private var currentLanguage: String? = null
    private var profilePicturePath: String? = null
    private var appLanguage = Locale.getDefault().language
    private var requestCode = 0
    private var SECOND_ACTIVITY_REQUEST_CODE = 12
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        appLanguage = intent.getStringExtra(LANGUAGE_KEY).toString()

        val sharedPreferences: SharedPreferences = getSharedPreferences("profilePicturePathConfig", MODE_PRIVATE)
        profilePicturePath = sharedPreferences.getString("path","")

        Log.i("PATHMENU",profilePicturePath.toString())

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appLanguage = intent.getStringExtra(LANGUAGE_KEY).toString()

        val bMap = BitmapFactory.decodeFile(profilePicturePath)
        binding.btnProfile!!.setImageBitmap(bMap)
        //setPic(binding.btnProfile!!, profilePicturePath!!)

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, GameModeActivity::class.java)
            startActivity(intent)
        }

        binding.btnProfile!!.setOnClickListener {
            val intent = ConfigImageActivity.getGalleryIntent(this)

            val editor = sharedPreferences.edit()
            editor.putString("path",profilePicturePath)
            editor.apply()
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

    private fun changeLanguage() {
        val languages = arrayOf("English", "Português")

        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.msg_choose_language)
            .setSingleChoiceItems(languages, -1) { _, which ->
                if (which == 0) {
                    setLocale("en")
                } else if (which == 1) {
                    setLocale("pt")
                }
            }
            .setNegativeButton(R.string.msg_cancel, null)
            .show()
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
                Companion.getIntent(this, l)
            )
            startActivity(intent)
        } else
            Toast.makeText(this, R.string.msg_error_language, Toast.LENGTH_LONG).show()
    }
}