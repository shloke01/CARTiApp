package hu.ait.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shoppingtable")
data class shoppingItem(
    @PrimaryKey(autoGenerate = true) val itemid: Long?,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "itemPrice") var itemPrice: String,
    @ColumnInfo(name = "itemDescription") var itemDescription: String,
    @ColumnInfo(name = "itemCategory") var itemCategory: String,
    @ColumnInfo(name = "isBought") var isBought: Boolean
) : Serializable