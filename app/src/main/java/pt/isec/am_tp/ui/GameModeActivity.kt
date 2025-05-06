package pt.isec.am_tp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivityGamemodeBinding

class GameModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamemodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamemodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingle.setOnClickListener {
            val intent = Intent(this, SinglePlayerActivity::class.java)
            startActivity(intent)
        }
    }

}
