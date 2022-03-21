package magnalleexample.coffeshoptest.ui.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import magnalleexample.coffeshoptest.databinding.BasketItemBinding
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem
import magnalleexample.coffeshoptest.ui.menu.MenuListAdapter

class BasketListAdapter: ListAdapter<CoffeeShopMenuItem, RecyclerView.ViewHolder>(MenuListAdapter) {
    companion object DiffCallback : DiffUtil.ItemCallback<CoffeeShopMenuItem>(){
        override fun areItemsTheSame(oldItem: CoffeeShopMenuItem, newItem: CoffeeShopMenuItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoffeeShopMenuItem, newItem: CoffeeShopMenuItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BasketItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BasketItemViewHolder -> {
                val item: BasketItemViewHolder = holder
                item.bind(getItem(position))
            }
        }
    }
    class BasketItemViewHolder private constructor (val binding: BasketItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): BasketItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BasketItemBinding.inflate(layoutInflater, parent, false)
                return BasketItemViewHolder(binding)
            }
        }
        fun bind(data: CoffeeShopMenuItem){

            binding.menuItemNameTextView.text = data.name
            binding.menuItemPriceTextView.text = data.price.toString()

            binding.menuItemAmountTextView.text = data.amount.toString()

            binding.addTextView.setOnClickListener {
                data.amount++
                binding.menuItemAmountTextView.text = data.amount.toString()
            }

            binding.removeTextView.setOnClickListener {
                if(data.amount - 1 >= 0) {
                    data.amount--
                    binding.menuItemAmountTextView.text = data.amount.toString()
                }
            }
        }
    }
}