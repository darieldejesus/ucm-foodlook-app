package es.ucm.foodlook

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.room.Room
import es.ucm.foodlook.adapters.RecentResultAdapter
import es.ucm.foodlook.types.Recent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class MainFragment : Fragment() {
    private lateinit var listView : ListView
    private val coroutineContext: CoroutineContext = newSingleThreadContext("FOODLOOK")
    private val scope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.e("FOODLOOK_APP", "On resumen!!!!")
        scope.launch {
            val db = Room.databaseBuilder(
                requireActivity().applicationContext,
                AppDatabase::class.java, "foodlook"
            )
                .fallbackToDestructiveMigration()
                .build()

            val recentDao = db.recentDao()
            val recentList: List<es.ucm.foodlook.entities.Recent> = recentDao.getAll()
            val recentListForAdapter = ArrayList<Recent>();
            for (recent in recentList) {
                recentListForAdapter.add(Recent(recent.id, recent.name!!, recent.image!!))
            }
            val adapter = RecentResultAdapter(requireContext(), recentListForAdapter)
            requireActivity().runOnUiThread {
                listView.adapter = adapter
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById<ListView>(R.id.list_recent)
    }
}
