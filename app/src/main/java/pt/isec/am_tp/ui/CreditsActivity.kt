package pt.isec.am_tp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivityCreditsBinding

class CreditsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreditsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}
