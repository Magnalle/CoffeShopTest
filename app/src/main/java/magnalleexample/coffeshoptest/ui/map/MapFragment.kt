package magnalleexample.coffeshoptest.ui.map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.findBinding
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBindings
import com.yandex.mapkit.Animation

import com.yandex.mapkit.mapview.MapView

import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.android.synthetic.main.map_fragment.*
import magnalleexample.coffeshoptest.R
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.MapFragmentBinding
import magnalleexample.coffeshoptest.domain.CoffeeShopData
import magnalleexample.coffeshoptest.ui.coffeeShopsList.CoffeeShopsListFragmentDirections


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
            val view = View(requireContext()).apply {
                background = requireContext().getDrawable(R.drawable.placemark)
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

    override fun onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        binding.mapview.onStart()
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

}