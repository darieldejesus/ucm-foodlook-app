package es.ucm.foodlook

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.commit
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.ucm.foodlook.entities.Recent
import es.ucm.foodlook.types.Dish
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 100
    private var check: Boolean = false
    private lateinit var camera: FloatingActionButton
    private lateinit var currentPhotoPath: String
    private var dialog : AlertDialog? = null;
    private val coroutineContext: CoroutineContext = newSingleThreadContext("FOODLOOK")
    private val scope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var builder = AlertDialog.Builder(this);
        builder.setView(layoutInflater.inflate(R.layout.loading_spinner, null))
        dialog = builder.create()

        camera = findViewById(R.id.camera)

        initPermissions()

        camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Log.e("FOODLOOK_APP", ex.toString())
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "es.ucm.foodlook.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, REQUEST_CODE)
            }
        }

        supportFragmentManager.commit {
            replace(R.id.container, FirstFragment())
        }
    }

    private fun showErrorAlert() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Algo no esta bien")
        builder.setMessage("No fue posible procesar el menú")
        val alert = builder.create()
        alert.show()
    }

    private fun loadMainFragment(recentList: ArrayList<Recent>) {
        var fragment = MainFragment().apply {
            arguments = Bundle().apply {
                putSerializable("recent", recentList)
            }
        }
        supportFragmentManager.commit {
            replace(R.id.container, fragment)
        }
    }

    private fun loadRecent() {
        Thread {
            val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "foodlook"
                )
                .fallbackToDestructiveMigration()
                .build()

            val recentDao = db.recentDao()
            //recentDao.insertAll(Recent("1", "Patatas", "https://post.healthline.com/wp-content/uploads/2020/09/healthy-eating-ingredients-732x549-thumbnail.jpg"))
            val recentList: List<Recent> = recentDao.getAll()
            if (recentList.isNotEmpty()) {
                loadMainFragment(ArrayList(recentList));
            }
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        scope.launch {
            if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
                runOnUiThread {
                    dialog!!.show()
                }

                var signedUrl = onGetSignedUrl()
                Log.i("FOODLOOK_APP", "Given signed URL $signedUrl")
                if (signedUrl != null) {
                    onUploadImage(signedUrl.url)
                    getListOfResults(signedUrl.fileName)
                } else {
//                    runOnUiThread {
//                        showErrorAlert()
//                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        runBlocking { // this: CoroutineScope
            launch { loadRecent() }
        }
    }

    private fun getListOfResults(fileName: String) {
        val handler = Handler(Looper.getMainLooper())
        var times = 0
        var runnable = object : Runnable {
            override fun run() {
                Log.i("FOODLOOK_APP", "Getting list of results...")
                var detections = onGetResults(fileName)
                Log.i("FOODLOOK_APP", "Given list of results $detections")
                times++
                if (detections == null) {
                    Log.i("FOODLOOK_APP", "No entries found. Retrying...")
                    if (times < 3) {
                        handler.postDelayed(this, 5000)
                    } else {
                        Log.i("FOODLOOK_APP", "Timeout... :(")
                    }
                } else {
                    Log.i("FOODLOOK_APP", "Entries found. Continuing...")
                    println(detections)
                }
            }
        }
        handler.postDelayed(runnable, 5000)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun onUploadImage(presignedUrl: String) {
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val file = File(currentPhotoPath)
//                    var file = SaveImage(image)
                    val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull());
                    val okHttpClient = OkHttpClient()
                        .newBuilder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build()
                    val request = Request.Builder()
                        .url(presignedUrl)
                        .put(requestBody)
                        .build();
                    var resp = okHttpClient.newCall(request).execute()
                    Log.e("FOODLOOK_APP", "Image successfully uploaded $resp")
                } catch (e: Exception) {
                    Log.e("FOODLOOK_APP", "Unable to upload image $e")
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun onGetSignedUrl(): GeneratePresignedUrlMutation.PresignedUrl? {
        var signedUrl : GeneratePresignedUrlMutation.PresignedUrl? = null
        Log.e("FOODLOOK_APP", "Generating presigned URL...")
        var apolloClient = ApolloClient.Builder()
            .serverUrl("https://be80mdd0f0.execute-api.us-east-1.amazonaws.com/dev/graphql")
            .build()
        val response = apolloClient.mutation(GeneratePresignedUrlMutation()).execute()
        signedUrl = response.data?.presignedUrl
        Log.e("FOODLOOK_APP", "Generated presigned URL: ${signedUrl?.url}")
        return signedUrl
    }

    fun onGetResults(fileName: String): GetDetectionsQuery.Detection? {
        var detection : GetDetectionsQuery.Detection? = null
        runBlocking {
            var apolloClient = ApolloClient.Builder().serverUrl("https://be80mdd0f0.execute-api.us-east-1.amazonaws.com/dev/graphql").build()
            val response = apolloClient.query(GetDetectionsQuery(fileName)).execute()
            detection = response.data?.detection
            Log.e("FOODLOOK_APP", "Detected Lines: ${detection?.fileName}")
            val listOfDishes = ArrayList<Dish>()
            for (i in detection?.lines!!) {
                Log.e("FOODLOOK_APP", "Item ${i} has ${i.query}")
                listOfDishes.add(Dish(id = i.uuid, name = i.query))
            }
            runOnUiThread {
                dialog!!.hide()
            }
            onShowResults(listOfDishes)
        }
        return detection
    }

    fun onShowResults(dishes: ArrayList<Dish>) {
        val intent = Intent(this, ResultActivity::class.java).apply {}
        intent.putExtra("dishes", dishes)
        startActivity(intent)
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
                    Log.i("FOODLOOK_APP: ", "Permission has been denied by user")
                    Toast.makeText(this, "It is not allowed to use the camera.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i("FOODLOOK_APP: ", "Permission has been granted by user")
                    check = true
                }
            }
        }
    }
}