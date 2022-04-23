package hu.ait.shoppinglist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import hu.ait.shoppinglist.adapter.ShoppingAdapter
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.shoppingItem
import hu.ait.shoppinglist.databinding.ActivityScrollingBinding
import hu.ait.shoppinglist.dialog.descriptionDialog
import hu.ait.shoppinglist.dialog.shoppingDialog
import hu.ait.shoppinglist.touch.ShoppingRecyclerTouchCallback
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import kotlin.concurrent.thread

class ScrollingActivity : AppCompatActivity(), shoppingDialog.ShoppingItemHandler {

    companion object {
        const val KEY_ITEM_EDIT = "KEY_TODO_EDIT"
        const val KEY_STARTED_BEFORE = "KEY_STARTED_BEFORE"
        const val PREFNAME = "MYPREFERENCES"
    }

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: ShoppingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            shoppingDialog().show(supportFragmentManager, "SHOPPING_DIALOG")
        }

        binding.fabDeleteAll.setOnClickListener { view ->
            thread {
                AppDatabase.getInstance(this).shoppingItemDao().deleteAll()
            }
        }

        initRecyclerView()

        if (!wasStartedBefore()) {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(binding.fab)
                .setPrimaryText(getString(R.string.new_shopping_item))
                .setSecondaryText(getString(R.string.click_to_add))
                .show()

            saveAppWasStarted()
        }
    }

    private fun saveAppWasStarted() {
        val sharedpref = getSharedPreferences(PREFNAME, MODE_PRIVATE)
        val editor = sharedpref.edit()
        editor.putBoolean(KEY_STARTED_BEFORE, true)
        editor.apply()
    }

    private fun wasStartedBefore() : Boolean {
        val sharedpref = getSharedPreferences(PREFNAME, MODE_PRIVATE)
        return sharedpref.getBoolean(KEY_STARTED_BEFORE, false)
    }

    private fun initRecyclerView() {
        adapter = ShoppingAdapter(this)
        binding.recyclerShopping.adapter = adapter

        val touchCallbackList = ShoppingRecyclerTouchCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(binding.recyclerShopping)

        val shoppingItems = AppDatabase.getInstance(this).shoppingItemDao().getAllItems()
        shoppingItems.observe(this, Observer { items ->
            adapter.submitList(items)
        })
    }

    fun showDescriptionDialog(itemToEdit: shoppingItem) {
        // show dialog
        val dialog = descriptionDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_EDIT, itemToEdit)
        dialog.arguments = bundle

        dialog.show(supportFragmentManager, "TAG_ITEM_DESCRIPTION")
    }

    fun showEditDialog(itemToEdit: shoppingItem) {
        // show dialog
        val dialog = shoppingDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_EDIT, itemToEdit)
        dialog.arguments = bundle

        dialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }


    override fun shoppingItemCreated(item: shoppingItem) {
        thread {
            AppDatabase.getInstance(this).shoppingItemDao().insertItem(item)

            runOnUiThread {
                Snackbar.make(binding.root, getString(R.string.item_created), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        adapter.deleteLastItem()
                    }
                    .show()
            }
        }
    }

    override fun shoppingItemUpdated(item: shoppingItem) {
        thread {
            // Update todoItem
            AppDatabase.getInstance(this).shoppingItemDao().updateItem(item)
        }
    }

}