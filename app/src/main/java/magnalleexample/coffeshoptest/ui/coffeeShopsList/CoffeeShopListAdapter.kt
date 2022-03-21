package magnalleexample.coffeshoptest.ui.coffeeShopsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import magnalleexample.coffeshoptest.databinding.CoffeeShopsListItemBinding
import magnalleexample.coffeshoptest.domain.CoffeeShopData

class CoffeeShopListAdapter(private val clickListener : CoffeeShopDataClickListener): ListAdapter<CoffeeShopData, RecyclerView.ViewHolder>(DiffCallback)  {
    companion object DiffCallback : DiffUtil.ItemCallback<CoffeeShopData>(){
        override fun areItemsTheSame(oldItem: CoffeeShopData, newItem: CoffeeShopData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CoffeeShopData, newItem: CoffeeShopData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoffeeShopDataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CoffeeShopDataViewHolder -> {
                val item: CoffeeShopDataViewHolder = holder
                item.bind(getItem(position), clickListener)
            }
        }
    }
    class CoffeeShopDataViewHolder private constructor (val binding: CoffeeShopsListItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): CoffeeShopDataViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CoffeeShopsListItemBinding.inflate(layoutInflater, parent, false)
                return CoffeeShopDataViewHolder(binding)
            }
        }
        fun bind(data: CoffeeShopData, clickListener : CoffeeShopDataClickListener){
            binding.coffeeShopNameTextView.text = data.name
            if(data.dist < 0.001)
                binding.coffeeShopDistanceTextView.text = ""
            else {
                binding.coffeeShopDistanceTextView.text = "в ${(data.dist / 1000).toInt().toString()} км ${(data.dist - (data.dist / 1000).toInt() * 1000.0).toInt()} м от Вас"
            }
            binding.coffeeShopCardView.setOnClickListener{
                clickListener.onClick(data)
            }
        }
    }
}

class CoffeeShopDataClickListener(private val clickListener : (data: CoffeeShopData) -> Unit){
    fun onClick(data: CoffeeShopData) = clickListener.invoke(data)
}