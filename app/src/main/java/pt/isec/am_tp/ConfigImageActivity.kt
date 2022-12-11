package pt.isec.am_tp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import pt.isec.am_tp.databinding.ActivityProfileBinding


class ConfigImageActivity : AppCompatActivity(){
    private var mode = GALLERY
    private var imagePath : String? = null

    private var permissionsGranted = false
        set(value) {
            field = value
            binding.btnChange.isEnabled = value
        }

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_OK)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getIntExtra(MODE_KEY, GALLERY)
        val sharedPreferences: SharedPreferences = getSharedPreferences("profilePicturePathConfig", MODE_PRIVATE)
        imagePath = sharedPreferences.getString("path","")
        if(imagePath != "" &&imagePath != null){
            val bMap= BitmapFactory.decodeFile(imagePath)
            binding.frPreview.background = BitmapDrawable(resources,bMap)
        }
        else{
            binding.frPreview.background = ResourcesCompat.getDrawable(resources,
                android.R.drawable.ic_menu_report_image,
                null)
        }

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

    companion object {

        private const val PERMISSIONS_REQUEST_CODE = 1
        private const val GALLERY = 1
        private const val MODE_KEY = "mode"
        fun getGalleryIntent(context : Context) : Intent {
            val intent = Intent(context,ConfigImageActivity::class.java)
            intent.putExtra(MODE_KEY, GALLERY)
            return intent
        }
    }
    fun updatePreview() {
        if (imagePath != null) {
            setImage(binding.frPreview, imagePath!!)

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
    private var startActivityForContentResult = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri ->

        imagePath = uri?.let { createFileFromUri(this, it) }
        Log.i("IMAGEPATH", imagePath.toString())
        SavePath(imagePath);
        updatePreview()
    }
    private fun SavePath(result: String?) {
        var sharedPreferences: SharedPreferences = getSharedPreferences("profilePicturePathConfig", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("path",imagePath)
        editor.apply()
        Log.i("RESULTIMAGE",result.toString())

    }
}