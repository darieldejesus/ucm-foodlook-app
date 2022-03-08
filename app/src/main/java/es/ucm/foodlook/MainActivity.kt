package es.ucm.foodlook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.ucm.foodlook.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 100
    private var check: Boolean = false
    private lateinit var imageView: ImageView
    private lateinit var camera: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        camera = findViewById(R.id.camera)

        initPermissions()

        camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            imageView.setImageBitmap(data.extras!!.get("data") as Bitmap)
        }
    }

    // Permissions
    private fun initPermissions(){
        if(!getPermission()) setPermission()
        else check = true
    }
    private fun getPermission(): Boolean{
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    private fun setPermission(){
        val permissionsList = listOf<String>(
            Manifest.permission.CAMERA
        )
        ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), 1)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission: ", "Permission has been denied by user")
                    Toast.makeText(this, "It is not allowed to use the camera.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i("Permission: ", "Permission has been granted by user")
                    check = true
                }
            }
        }
    }
}