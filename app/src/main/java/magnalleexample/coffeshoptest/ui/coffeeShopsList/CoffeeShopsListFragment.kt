package magnalleexample.coffeshoptest.ui.coffeeShopsList

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import magnalleexample.coffeshoptest.R
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.CoffeeShopsListFragmentBinding
import com.google.android.gms.location.*

class CoffeeShopsListFragment : Fragment() {

    companion object {
        fun newInstance() = CoffeeShopsListFragment()
    }

    val viewModel: CoffeeShopsListViewModel by lazy {
        ViewModelProvider(this).get(CoffeeShopsListViewModel::class.java)
    }

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()
                viewModel.location.postValue(location)
                fusedLocationProvider?.removeLocationUpdates(this)
            }
        }
    }

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 2
        fastestInterval = 2
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : CoffeeShopsListFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.coffee_shops_list_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Ближайшие кофейни"
        // location
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this.requireContext())
        checkLocationPermission()

        val coffeeShopsListAdapter = CoffeeShopListAdapter(CoffeeShopDataClickListener{
            viewModel.onCoffeeShopClick(it)
        })
        binding.coffeeShopListRecycleView.adapter = coffeeShopsListAdapter
        binding.coffeeShopListRecycleView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.mapButton.setOnClickListener {
            findNavController().navigate(
                CoffeeShopsListFragmentDirections.actionCoffeeShopsListFragmentToMapFragment()
            )
        }

        viewModel.updateCoffeeShopsList.observe(viewLifecycleOwner, Observer {
            if(it == true){
                coffeeShopsListAdapter.submitList(application.app.coffeeShopsList)
                coffeeShopsListAdapter.notifyDataSetChanged()
                viewModel.coffeeShopsListLoaded()
            }
        })

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

        viewModel.navigateToLogin.observe(viewLifecycleOwner, {
            if(it == true){
                findNavController().navigate(
                    CoffeeShopsListFragmentDirections.actionCoffeeShopsListFragmentToLoginFragment())
                viewModel.disableNavigation()
            }
        })

        viewModel.navigateToMenu.observe(viewLifecycleOwner, {
            it?.let{
                findNavController().navigate(
                    CoffeeShopsListFragmentDirections.actionCoffeeShopsListFragmentToMenuFragment(it))
                viewModel.disableNavigation()
            }
        })

        viewModel.loadData()

        viewModel.location.observe(viewLifecycleOwner, {
            viewModel.updateDistance()
        })

        return binding.root
    }

    private fun checkLocationPermission() {
        if (checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this.requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
                permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.
                    } else -> {
                    Toast.makeText(this.requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun checkBackgroundLocation(){
        if (checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                requireActivity().mainLooper)
        }
    }
}