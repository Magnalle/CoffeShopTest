package magnalleexample.coffeshoptest.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import magnalleexample.coffeshoptest.databinding.MenuItemBinding
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem

class MenuListAdapter : ListAdapter<CoffeeShopMenuItem, RecyclerView.ViewHolder>(MenuListAdapter){
    companion object DiffCallback : DiffUtil.ItemCallback<CoffeeShopMenuItem>(){
        override fun areItemsTheSame(oldItem: CoffeeShopMenuItem, newItem: CoffeeShopMenuItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoffeeShopMenuItem, newItem: CoffeeShopMenuItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MenuItemViewHolder -> {
                val item: MenuItemViewHolder = holder
                item.bind(getItem(position))
            }
        }
    }
    class MenuItemViewHolder private constructor (val binding: MenuItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): MenuItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MenuItemBinding.inflate(layoutInflater, parent, false)
                return MenuItemViewHolder(binding)
            }
        }
        fun bind(data: CoffeeShopMenuItem){

            binding.menuItemNameTextView.text = data.name
            binding.menuItemPriceTextView.text = data.price.toString()
            Glide
                .with(binding.root)
                .load(data.imageURL)
                .centerInside()
                .placeholder(CircularProgressDrawable(binding.root.context).let {
                    it.start()
                    it})
                .into(binding.menuItemImageView)

            binding.menuItemAmountTextView.text = data.amount.toString()
            binding.menuItemImageView
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