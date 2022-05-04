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
        var listOfImages = arguments?.getSerializable("images") as ArrayList<DishImage>
        val adapter = DishImagesResultAdapter(requireContext(), listOfImages)
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
