package pt.isec.am_tp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivityCreditsBinding
import pt.isec.am_tp.databinding.ActivityGamemodeBinding

class CreditsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreditsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}
