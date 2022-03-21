package magnalleexample.coffeshoptest.ui.coffeeShopsList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import magnalleexample.coffeshoptest.R
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.CoffeeShopsListFragmentBinding
import magnalleexample.coffeshoptest.databinding.LoginFragmentBinding
import magnalleexample.coffeshoptest.ui.login.LoginFragmentDirections

class CoffeeShopsListFragment : Fragment() {

    companion object {
        fun newInstance() = CoffeeShopsListFragment()
    }

    val viewModel: CoffeeShopsListViewModel by lazy {
        ViewModelProvider(this).get(CoffeeShopsListViewModel::class.java)
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
            it?.let{
                coffeeShopsListAdapter.submitList(application.app.coffeeShopsList)
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

        return binding.root
    }

}