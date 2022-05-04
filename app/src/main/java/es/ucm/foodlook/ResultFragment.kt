package es.ucm.foodlook

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.apollographql.apollo3.ApolloClient
import es.ucm.foodlook.adapters.DishesResultAdapter
import es.ucm.foodlook.databinding.FragmentResultBinding
import es.ucm.foodlook.entities.Recent
import es.ucm.foodlook.types.Dish
import es.ucm.foodlook.types.DishImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var listView : ListView
    private var dialog : AlertDialog? = null;
    private val coroutineContext: CoroutineContext = newSingleThreadContext("FOODLOOK")
    private val scope = CoroutineScope(coroutineContext)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResultBinding.inflate(inflater, container, false)

        var builder = AlertDialog.Builder(activity);
        builder.setView(layoutInflater.inflate(R.layout.loading_spinner, null))
        dialog = builder.create()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var dishes = arguments?.getSerializable("dishes") as ArrayList<Dish>
        listView = view.findViewById<ListView>(R.id.list_dishes)
        val adapter = DishesResultAdapter(requireContext(), dishes)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val dish = dishes.get(position)
            if (dish == null) {
                null
            }
            val listOfImages = java.util.ArrayList<DishImage>()
            scope.launch {
                ThreadUtils.runOnUiThread {
                    dialog!!.show()
                }
                var apolloClient = ApolloClient.Builder()
                    .serverUrl("https://be80mdd0f0.execute-api.us-east-1.amazonaws.com/dev/graphql")
                    .build()
                println("A buscar ${dish.id}")
                val response = apolloClient.query(GetImagesQuery(dish.id)).execute()
                val images = response.data?.images
                for (i in images!!) {
                    listOfImages.add(DishImage(id = i.uuid, url = i.url))
                }

                if (listOfImages.isNotEmpty()) {
                    saveRecent(dish, listOfImages.first())
                }

                var fragment = ImageResultFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("images", listOfImages)
                    }
                }
                ThreadUtils.runOnUiThread {
                    dialog!!.hide()
                }
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.result_container, fragment)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveRecent(dish: Dish, image: DishImage) {
        val db = Room.databaseBuilder(
            requireContext()!!.applicationContext,
            AppDatabase::class.java, "foodlook"
        )
            .fallbackToDestructiveMigration()
            .build()

        val recentDao = db.recentDao()
        //recentDao.insertAll(Recent("1", "Patatas", "https://post.healthline.com/wp-content/uploads/2020/09/healthy-eating-ingredients-732x549-thumbnail.jpg"))
        val recentFound = recentDao.findById(dish.id)
        if (recentFound == null) {
            val insert = Recent(dish.id, dish.name, image.url)
            recentDao.insertAll(insert)
        }
    }
}