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
        listOfDishes.add(DishImage(url = "https://www.expatica.com/app/uploads/sites/2/2016/09/spanish-food.jpg"))
        listOfDishes.add(DishImage(url = "https://thumbor.thedailymeal.com/RMy0wX6kVNVYQPH0Qgt6Q3HBSEc=//https://www.thedailymeal.com/sites/default/files/2020/05/28/iconic_dishes_hero.jpg"))
        listOfDishes.add(DishImage(url = "https://www.expatica.com/app/uploads/sites/2/2016/09/croquetas.jpg"))
        listOfDishes.add(DishImage(url = "https://upload.wikimedia.org/wikipedia/commons/5/59/A_traditional_indian_dish_of_bengal%28pulao-mangsha_with_misti_doi%29.jpg"))
        listOfDishes.add(DishImage(url = "https://images.squarespace-cdn.com/content/v1/56637e51e4b00229eb3a9b26/1464248461181-UO96AHY9FRY52EQSYTJW/burrata.jpg?format=2500w"))
        listOfDishes.add(DishImage(url = "https://www.expatica.com/app/uploads/sites/2/2016/09/fabada.jpg"))
        listOfDishes.add(DishImage(url = "https://www.ourescapeclause.com/wp-content/uploads/2019/08/Ireland-46-1.jpg"))
        listOfDishes.add(DishImage(url = "https://media-cldnry.s-nbcnews.com/image/upload/t_fit-1500w,f_auto,q_auto:best/mpx/2704722219/2022_03/1647354072907_tdy_food_9a_adam_richman_220315_1920x1080-nnlm32.jpg"))
        listOfDishes.add(DishImage(url = "https://a.cdn-hotels.com/gdcs/production109/d913/52127df7-ccff-4762-8255-01b3ba749fca.jpg"))
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
