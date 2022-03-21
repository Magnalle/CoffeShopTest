package magnalleexample.coffeshoptest.ui.basket

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import magnalleexample.coffeshoptest.R
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.BasketFragmentBinding
import magnalleexample.coffeshoptest.databinding.CoffeeShopsListFragmentBinding
import magnalleexample.coffeshoptest.ui.coffeeShopsList.CoffeeShopDataClickListener
import magnalleexample.coffeshoptest.ui.coffeeShopsList.CoffeeShopListAdapter

class BasketFragment : Fragment() {

    companion object {
        fun newInstance() = BasketFragment()
    }

    private lateinit var viewModel: BasketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Ваш заказ"
        val binding : BasketFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.basket_fragment, container, false
        )

        val basketListAdapter = BasketListAdapter()
        binding.basketRecycleView.adapter = basketListAdapter
        binding.basketRecycleView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        basketListAdapter.submitList(viewModel.loadData())
        basketListAdapter.notifyDataSetChanged()

        return binding.root
    }

}