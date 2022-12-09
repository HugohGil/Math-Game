package pt.isec.am_tp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivitySingleplayerBinding

class SinglePlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleplayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
