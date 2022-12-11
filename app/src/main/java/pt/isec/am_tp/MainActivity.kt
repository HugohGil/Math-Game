package pt.isec.am_tp


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.am_tp.databinding.ActivityMenuBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultLanguage()                // first time app running
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
    private fun setDefaultLanguage() {
        if (Locale.getDefault().language == "pt")
            setLocale("pt")
        else
            setLocale("en")
    }
    private fun setLocale(l: String) {
            val locale = Locale(l)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
    }
}