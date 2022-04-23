package hu.ait.shoppinglist.touch

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import hu.ait.shoppinglist.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class ShoppingRecyclerTouchCallback(private val shoppingTouchHelperAdapter: ShoppingTouchHelperCallback) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        shoppingTouchHelperAdapter.onItemMoved(
            viewHolder.adapterPosition,
            target.adapterPosition
        )

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        shoppingTouchHelperAdapter.onDismissed(viewHolder.adapterPosition)
    }

}
