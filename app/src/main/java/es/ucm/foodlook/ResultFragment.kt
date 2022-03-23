package es.ucm.foodlook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import es.ucm.foodlook.adapters.DishesResultAdapter
import es.ucm.foodlook.databinding.FragmentResultBinding
import es.ucm.foodlook.types.Dish

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var listView : ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResultBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById<ListView>(R.id.list_dishes)
        val listOfDishes = ArrayList<Dish>()
        listOfDishes.add(Dish(name = " Sarten con huevos rotos "))
        listOfDishes.add(Dish(name = "Virutas de Foie"))
        listOfDishes.add(Dish(name = "Alcachofas a la plancha"))
        listOfDishes.add(Dish(name = "Bienmesabe de pollo"))
        listOfDishes.add(Dish(name = "Trío de verduras"))
        listOfDishes.add(Dish(name = "Canelón casero"))
        listOfDishes.add(Dish(name = "Tataki de atún"))
        listOfDishes.add(Dish(name = "Ensalada caprese"))
        listOfDishes.add(Dish(name = "Merluza a la romana"))
        val adapter = DishesResultAdapter(requireContext(), listOfDishes)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            var fragment = ImageResultFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.result_container, fragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_First2Fragment_to_Second2Fragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}