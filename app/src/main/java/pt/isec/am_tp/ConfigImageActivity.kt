package pt.isec.am_tp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import pt.isec.am_tp.databinding.ActivityProfileBinding
import java.io.Serializable


class ConfigImageActivity : AppCompatActivity(){
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getIntExtra(MODE_KEY, GALLERY)
        when (mode) {
            GALLERY -> binding.btnChange.apply {
                text = getString(R.string.choose_image)
                setOnClickListener { chooseImage() }
            }
            else -> finish()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        verifyPermissions()
        updatePreview()
    }
    private var mode = GALLERY
    private var imagePath : String? = null
    private var permissionsGranted = false
        set(value) {
            field = value
            binding.btnChange.isEnabled = value
        }

    companion object {
        private const val ACTIVITY_REQUEST_CODE_GALLERY = 1
        private const val PERMISSIONS_REQUEST_CODE = 1
        private const val GALLERY = 1
        private const val MODE_KEY = "mode"
    }
    fun updatePreview() {
        if (imagePath != null) {
            setPic(binding.frPreview, imagePath!!)
            //returnResult(imagePath);
        }
        else
            binding.frPreview.background = ResourcesCompat.getDrawable(resources,
               android.R.drawable.ic_menu_report_image,
                null)
    }
    fun chooseImage() {
        startActivityForContentResult.launch("image/*")

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            permissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun verifyPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = false
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        } else
            permissionsGranted = true
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionsGranted = isGranted
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, info: Intent?) {
        if (requestCode == ACTIVITY_REQUEST_CODE_GALLERY && resultCode == RESULT_OK && info != null) {
            info.data?.let { uri ->
                imagePath = createFileFromUri(this,uri)
                updatePreview()
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, info)

    }
    private var startActivityForContentResult = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri ->

        imagePath = uri?.let { createFileFromUri(this, it) }

        updatePreview()
    }
    private fun returnResult(result: String?) {
        val returnIntent = Intent()
        returnIntent.putExtra(result, result as Serializable)
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}