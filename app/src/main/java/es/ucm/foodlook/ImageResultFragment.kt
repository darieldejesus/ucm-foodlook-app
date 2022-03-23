package es.ucm.foodlook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import es.ucm.foodlook.adapters.DishImagesResultAdapter
import es.ucm.foodlook.adapters.DishesResultAdapter
import es.ucm.foodlook.databinding.FragmentImageResultBinding
import es.ucm.foodlook.types.Dish
import es.ucm.foodlook.types.DishImage

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ImageResultFragment : Fragment() {

    private var _binding: FragmentImageResultBinding? = null

    private lateinit var listView : ListView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentImageResultBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById<ListView>(R.id.list_images)
        val listOfDishes = ArrayList<DishImage>()
        listOfDishes.add(DishImage(url = "https://loveincorporated.blob.core.windows.net/contentimages/gallery/d9e900e4-212e-4c3d-96d5-cb14a023c659-worlds-most-delicious-dishes.jpg"))
        listOfDishes.add(DishImage(url = "https://thumbor.thedailymeal.com/RMy0wX6kVNVYQPH0Qgt6Q3HBSEc=//https://www.thedailymeal.com/sites/default/files/2020/05/28/iconic_dishes_hero.jpg"))
        listOfDishes.add(DishImage(url = "https://loveincorporated.blob.core.windows.net/contentimages/gallery/e357b234-9ff0-4aff-a200-ba60f70dbf12-41-plov.jpg"))
        listOfDishes.add(DishImage(url = "https://upload.wikimedia.org/wikipedia/commons/5/59/A_traditional_indian_dish_of_bengal%28pulao-mangsha_with_misti_doi%29.jpg"))
        listOfDishes.add(DishImage(url = "https://images.squarespace-cdn.com/content/v1/56637e51e4b00229eb3a9b26/1464248461181-UO96AHY9FRY52EQSYTJW/burrata.jpg?format=2500w"))
        listOfDishes.add(DishImage(url = "http://cdn.cnn.com/cnnnext/dam/assets/160929095729-essential-spanish-dish-fideua-brindisa.jpg"))
        listOfDishes.add(DishImage(url = "http://cdn.cnn.com/cnnnext/dam/assets/160929101749-essential-spanish-dish-paella-phaidon.jpg"))
        listOfDishes.add(DishImage(url = "https://www.gastronomicspain.com/blog/wp-content/uploads/2019/10/Cocido-madrile%C3%B1o-gastronomic-SPain-3.jpg"))
        listOfDishes.add(DishImage(url = "https://estaticos.esmadrid.com/cdn/farfuture/KX-_RcG4PwL9B2ubqegADLGPGZLWLkxeLg7XqlcqE9U/mtime:1646729549/sites/default/files/styles/content_type_full/public/eventos/eventos/cocido_2.jpg?itok=DJMTZSFp"))
        val adapter = DishImagesResultAdapter(requireContext(), listOfDishes)
        listView.adapter = adapter
//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
