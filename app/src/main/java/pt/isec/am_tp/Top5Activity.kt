package pt.isec.am_tp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isec.am_tp.databinding.ActivityTop5Binding

class Top5Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTop5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTop5Binding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}
