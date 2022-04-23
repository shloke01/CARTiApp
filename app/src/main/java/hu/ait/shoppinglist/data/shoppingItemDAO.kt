package hu.ait.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface shoppingItemDAO {

    @Query("SELECT * FROM shoppingtable")
    fun getAllItems() : LiveData<List<shoppingItem>>

    @Insert
    fun insertItem(insertItem: shoppingItem)

    @Delete
    fun deleteItem(deleteItem: shoppingItem)

    @Update
    fun updateItem(updateItem: shoppingItem)

    @Query("DELETE FROM shoppingtable")
    fun deleteAll()

}