package es.ucm.foodlook

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 100
    private var check: Boolean = false
    private lateinit var imageView: ImageView
    private lateinit var camera: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //imageView = findViewById(R.id.imageView)
        camera = findViewById(R.id.camera)

        initPermissions()

        camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun SaveImage(image: Bitmap): File {
        var root = ContextWrapper(getApplicationContext())
        var dir = root.getDir("images", Context.MODE_PRIVATE)
        if (!dir.exists()) {
            dir.mkdir()
        }
        var name = "menu.jpg"
        var file = File(dir, name)
        try {
            var outputStream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch(e: Error) {
            e.printStackTrace()
        }
        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            var image = data.extras!!.get("data") as Bitmap
            //imageView.setImageBitmap(image)
            val intent = Intent(this, ResultActivity::class.java).apply {}
            startActivity(intent)
//            lifecycleScope.launch {
//                onUploadImage(image)
//            }
//            var fragment = FirstFragment()
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.add(R.id.container, fragment)
//            transaction.disallowAddToBackStack()
//            transaction.commit()
        }
    }

    suspend fun onUploadImage(image: Bitmap) {
        return withContext(Dispatchers.IO){
            try {
                var client = OkHttpClient();
                var file = SaveImage(image)
                val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull());
                val request = Request.Builder()
                    .url("https://food-look-api-dev-imagess3bucket-zk8jabmok60d.s3.amazonaws.com/images/2022/03/20220312-184431282-menu.jpeg?Content-Type=image%2Fpng&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAYTGKYVEV2UV4FAHE%2F20220312%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220312T184431Z&X-Amz-Expires=900&X-Amz-Signature=b96820c71b27d91ab87b10ddc67dcb5776ea3fa99569555ef487018e1d1a8ff8&X-Amz-SignedHeaders=host%3Bx-amz-acl&x-amz-acl=public-read")
                    .put(requestBody)
                    .build();
                var resp = client.newCall(request).execute()
                println("Unable to upload $resp")
            } catch (e: Exception) {
                println("Unable to upload " + e.message)
                e.printStackTrace()
            }
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