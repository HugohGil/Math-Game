package pt.isec.am_tp.ui


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.R
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
    private var profilePicturePath: String? = null
    private var appLanguage = Locale.getDefault().language
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        appLanguage = intent.getStringExtra(LANGUAGE_KEY).toString()

        val sharedPreferences: SharedPreferences = getSharedPreferences("profilePicturePathConfig", MODE_PRIVATE)
        profilePicturePath = sharedPreferences.getString("path","")

        Log.i("PATHMENU",profilePicturePath.toString())

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(profilePicturePath!= "" && profilePicturePath != null){
            val bMap = BitmapFactory.decodeFile(profilePicturePath)
            val imageButton : ImageButton = findViewById(R.id.btnProfile)
            imageButton.setImageBitmap(bMap)
        }

        appLanguage = intent.getStringExtra(LANGUAGE_KEY).toString()

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
                getIntent(this, l)
            )
            startActivity(intent)
        } else
            Toast.makeText(this, R.string.msg_error_language, Toast.LENGTH_LONG).show()
    }
}