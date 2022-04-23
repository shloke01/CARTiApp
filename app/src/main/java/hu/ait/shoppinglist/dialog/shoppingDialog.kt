package hu.ait.shoppinglist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.ScrollingActivity
import hu.ait.shoppinglist.data.shoppingItem
import hu.ait.shoppinglist.databinding.ItemRowBinding
import hu.ait.shoppinglist.databinding.ShoppingDialogBinding

class shoppingDialog : DialogFragment(), AdapterView.OnItemSelectedListener {

    interface ShoppingItemHandler {
        fun shoppingItemCreated(item: shoppingItem)
        fun shoppingItemUpdated(item: shoppingItem)
    }

    lateinit var shoppingItemHandler: ShoppingItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ShoppingItemHandler) {
            shoppingItemHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.not_implementing_literal)
            )
        }
    }

    lateinit var binding: ShoppingDialogBinding
    private var isEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())


        // Are we in edit mode? - Have we received a shoppingItem object to edit?
        if (arguments != null && requireArguments().containsKey(
                ScrollingActivity.KEY_ITEM_EDIT
            )
        ) {
            isEditMode = true
            dialogBuilder.setTitle(getString(R.string.edit_item))
        } else {
            isEditMode = false
            dialogBuilder.setTitle(getString(R.string.new_item))
        }

        binding = ShoppingDialogBinding.inflate(requireActivity().layoutInflater)
        dialogBuilder.setView(binding.root)

        val categoriesAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories_array, android.R.layout.simple_spinner_item
        )
        categoriesAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategories.adapter = categoriesAdapter
        binding.spinnerCategories.onItemSelectedListener = this

        // pre-fill the dialog if we are in edit mode
        if (isEditMode) {
            val itemToEdit =
                requireArguments().getSerializable(
                    ScrollingActivity.KEY_ITEM_EDIT
                ) as shoppingItem

            binding.etName.setText(itemToEdit.itemName)
            binding.etPrice.setText(itemToEdit.itemPrice)
            binding.etDescription.setText(itemToEdit.itemDescription)
            binding.spinnerCategories.setSelection(categoriesAdapter.getPosition(itemToEdit.itemCategory))
            binding.cbBought.isChecked = itemToEdit.isBought
        }

        dialogBuilder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
        }
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
        }

        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                binding.etName.error = getString(R.string.field_empty)
            } else if (binding.etPrice.text.isEmpty()){
                binding.etPrice.error = getString(R.string.field_empty)
            } else {
                if (isEditMode) {
                    val itemToEdit =
                        (requireArguments().getSerializable(
                            ScrollingActivity.KEY_ITEM_EDIT
                        ) as shoppingItem).copy(
                            itemName = binding.etName.text.toString(),
                            itemDescription = binding.etDescription.text.toString(),
                            itemPrice = binding.etPrice.text.toString(),
                            itemCategory = binding.spinnerCategories.selectedItem.toString(),
                            isBought = binding.cbBought.isChecked
                        )

                    shoppingItemHandler.shoppingItemUpdated(itemToEdit)
                } else {
                    shoppingItemHandler.shoppingItemCreated(
                        shoppingItem(
                            null,
                            binding.etName.text.toString(),
                            binding.etPrice.text.toString(),
                            binding.etDescription.text.toString(),
                            binding.spinnerCategories.selectedItem.toString(),
                            binding.cbBought.isChecked
                        )
                    )
                }

                dialog.dismiss()
            }
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var itemBinding = ItemRowBinding.inflate(layoutInflater)
        itemBinding.imgCategory.setImageResource(R.drawable.carti)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}