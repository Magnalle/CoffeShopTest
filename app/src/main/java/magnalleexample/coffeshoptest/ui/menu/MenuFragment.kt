package magnalleexample.coffeshoptest.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import magnalleexample.coffeshoptest.R
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.MenuFragmentBinding
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem
import magnalleexample.coffeshoptest.ui.coffeeShopsList.CoffeeShopsListFragmentDirections

class MenuFragment : Fragment() {

    companion object {
        fun newInstance() = MenuFragment()
    }

    lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : MenuFragmentBinding= DataBindingUtil.inflate(
            inflater, R.layout.menu_fragment, container, false
        )
        val arguments = MenuFragmentArgs.fromBundle(requireArguments())
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Меню ${arguments.coffeeShopData.name}"

        val menuListAdapter = MenuListAdapter()

        binding.menuRecycleView.adapter = menuListAdapter
        binding.menuRecycleView.layoutManager = GridLayoutManager(activity, 2)
        val application = requireNotNull(this.activity).application
        menuListAdapter.submitList(application.app.menuList)
        menuListAdapter.notifyDataSetChanged()

        binding.basketButton.setOnClickListener {
            viewModel.onBasketButtonClicked(arguments.coffeeShopData)
        }

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        viewModel.displayError.observe(viewLifecycleOwner, { value ->
            value?.let { value ->
                Toast.makeText(application, value, Toast.LENGTH_LONG).show()
                viewModel.disableError()
            }
        })

        viewModel.navigateToLogin.observe(viewLifecycleOwner, {
            if (it == true) {
                findNavController().navigate(
                    MenuFragmentDirections.actionMenuFragmentToLoginFragment()
                )
                viewModel.disableNavigation()
            }
        })

        viewModel.navigateToBasket.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(
                    MenuFragmentDirections.actionMenuFragmentToBasketFragment(arguments.coffeeShopData)
                )
                viewModel.disableNavigation()
            }
        })

        return binding.root
    }

}