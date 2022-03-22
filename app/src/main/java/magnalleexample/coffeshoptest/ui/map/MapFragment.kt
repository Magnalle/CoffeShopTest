package magnalleexample.coffeshoptest.ui.map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.Animation

import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.android.synthetic.main.placemark.view.*
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.MapFragmentBinding
import magnalleexample.coffeshoptest.domain.CoffeeShopData


class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel
    private val binding : MapFragmentBinding by lazy {
        MapFragmentBinding.inflate(layoutInflater)
    }

    private val markers : MutableList<Marker> = mutableListOf()
    private lateinit var mapObjectCollection : MapObjectCollection
    inner class Marker(
        val coffeeShopData: CoffeeShopData){
        val mapObject: PlacemarkMapObject = mapObjectCollection.addPlacemark(
            Point(
                coffeeShopData.point.latitude,
                coffeeShopData.point.longitude
            ))
        private val clickListener : (mapObject : MapObject, point : Point) -> Boolean =
            { mapObject, point ->
                viewModel.onCoffeeShopClick(coffeeShopData)
                false
            }
        init {
            val view = PlacemarkView(requireContext()).apply {
                this.placemark_name_text_view.text = coffeeShopData.name
            }
            mapObject.setView(ViewProvider(view))
            mapObject.addTapListener(clickListener)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        val application = requireNotNull(this.activity).application
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Карта"

        if(!application.app.coffeeShopsList.isEmpty()) {
            binding.mapview.getMap().move(
                CameraPosition(
                    Point(
                        application.app.coffeeShopsList[0].point.latitude,
                        application.app.coffeeShopsList[0].point.longitude
                    ), 20.0f, 0.0f, 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 0F),
                null
            )
            mapObjectCollection = binding.mapview.map.mapObjects.addCollection()
            for (place in application.app.coffeeShopsList) {
                val marker = Marker(place)
                markers.add(marker)
            }
        }

        viewModel.displayError.observe(viewLifecycleOwner, { value ->
            value?.let { value ->
                Toast.makeText(application, value, Toast.LENGTH_LONG).show()
                viewModel.disableError()
            }
        })

        viewModel.menuUpdating.observe(viewLifecycleOwner, {
            it?.let{
                viewModel.updateMenu(it)
            }
        })

        viewModel.navigateToMenu.observe(viewLifecycleOwner, {
            it?.let{
                findNavController().navigate(
                    MapFragmentDirections.actionMapFragmentToMenuFragment(it)
                )
                viewModel.disableNavigation()
            }
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api_key = "23d1f436-89f0-46cd-adef-62795ac28e04"
        if(!requireActivity().application.app.apiKeySet)
            MapKitFactory.setApiKey(api_key)
        requireActivity().application.app.apiKeySet = true
        MapKitFactory.initialize(requireContext())
    }

    override fun onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        binding.mapview.map.mapObjects.clear()
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }
    override fun onStart() {
        super.onStart()
        binding.mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }

}