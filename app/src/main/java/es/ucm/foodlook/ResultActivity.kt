package es.ucm.foodlook

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import es.ucm.foodlook.databinding.ActivityResultBinding
import es.ucm.foodlook.types.Dish

class ResultActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        var dishes = intent.getSerializableExtra("dishes")

        var fragment = ResultFragment().apply {
            arguments = Bundle().apply {
                putSerializable("dishes", dishes)
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.result_container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}
