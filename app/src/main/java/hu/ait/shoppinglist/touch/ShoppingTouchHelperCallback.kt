package hu.ait.shoppinglist.touch

interface ShoppingTouchHelperCallback {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
