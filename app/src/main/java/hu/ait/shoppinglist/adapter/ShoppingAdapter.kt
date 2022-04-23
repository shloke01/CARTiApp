package hu.ait.shoppinglist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.ScrollingActivity
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.shoppingItem
import hu.ait.shoppinglist.databinding.ItemRowBinding
import hu.ait.shoppinglist.touch.ShoppingTouchHelperCallback
import kotlin.concurrent.thread

class ShoppingAdapter(var context: Context) :
    ListAdapter<shoppingItem, ShoppingAdapter.ViewHolder>(ShoppingItemDiffCallback()), ShoppingTouchHelperCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val shoppingItemBinding =
            ItemRowBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(shoppingItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    fun deleteLastItem() {
        deleteItem(itemCount - 1)
    }

    private fun deleteItem(idx: Int) {
        thread {
            AppDatabase.getInstance(context).shoppingItemDao().deleteItem(getItem(idx))
        }
    }


    inner class ViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: shoppingItem) {
            binding.tvName.text = item.itemName
            binding.tvPrice.text = (context as ScrollingActivity).getString(R.string.price_with_argument, item.itemPrice)
            binding.cbBought.isChecked = item.isBought

            when(item.itemCategory) {
                "Food" -> binding.imgCategory.setImageResource(R.drawable.food)
                "Sports" -> binding.imgCategory.setImageResource(R.drawable.sports)
                "Electronics" -> binding.imgCategory.setImageResource(R.drawable.electronics)
                "Cleaning Supplies" -> binding.imgCategory.setImageResource(R.drawable.cleaning)
                "Other" -> binding.imgCategory.setImageResource(R.drawable.other)
            }

            binding.cbBought.setOnClickListener {
                val currentItem = getItem(adapterPosition)
                currentItem.isBought = binding.cbBought.isChecked

                thread {
                    AppDatabase.getInstance(context).shoppingItemDao().updateItem(currentItem)
                }
            }

            binding.btnEdit.setOnClickListener {
                (context as ScrollingActivity).showEditDialog(
                    getItem(this.adapterPosition)
                )
            }

            binding.btnDescription.setOnClickListener { view ->
                (context as ScrollingActivity).showDescriptionDialog(
                    getItem(this.adapterPosition)
                )
            }
        }
    }
}

class ShoppingItemDiffCallback : DiffUtil.ItemCallback<shoppingItem>() {
    override fun areItemsTheSame(oldItem: shoppingItem, newItem: shoppingItem): Boolean {
        return oldItem.itemid == newItem.itemid
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: shoppingItem, newItem: shoppingItem): Boolean {
        return oldItem == newItem
    }
}