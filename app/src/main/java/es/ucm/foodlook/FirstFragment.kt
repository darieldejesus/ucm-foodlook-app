package es.ucm.foodlook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import es.ucm.foodlook.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var LinearLayout: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var listView: ListView

    // This property is only valid between onCreateView and
    // onDestroyView.

    //listview = (ListView) findViewById(R.id.listview)
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = requireView().findViewById(R.id.ListView)
        var platos = arrayOf(
            " Sarten con huevos rotos ",
            " Virutas de Foie ",
            " Alcachofas a la plancha ",
            " Bienmesabe de pollo ",
            " Trío de verduras ",
            " Canelón casero ",
            " Tataki de atún ",
            " Ensalada caprese ",
            " Merluza a la romana ",
        )
        var adapter = ArrayAdapter(requireContext(), R.layout.dish_item, platos)
        listView.adapter = adapter
        //binding.ListView.setOnClickListener {
        //    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        //}
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}