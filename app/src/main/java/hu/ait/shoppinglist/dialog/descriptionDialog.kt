package hu.ait.shoppinglist.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.ScrollingActivity
import hu.ait.shoppinglist.data.shoppingItem
import hu.ait.shoppinglist.databinding.DescriptionDialogBinding
import hu.ait.shoppinglist.databinding.ShoppingDialogBinding

class descriptionDialog : DialogFragment() {

    lateinit var binding: DescriptionDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        val itemToEdit =
            requireArguments().getSerializable(
                ScrollingActivity.KEY_ITEM_EDIT
            ) as shoppingItem

        binding = DescriptionDialogBinding.inflate(requireActivity().layoutInflater)
        dialogBuilder.setView(binding.root)

        dialogBuilder.setTitle(getString(R.string.item_description))

        binding.descriptionName.text = getString(R.string.item_name_with_argument, itemToEdit.itemName)
        binding.descriptionPrice.text = getString(R.string.price_with_argument, itemToEdit.itemPrice)
        if (itemToEdit.itemDescription.isEmpty()){
            binding.descriptionDescription.text = getString(R.string.description_with_argument, "none")
        } else {
            binding.descriptionDescription.text = getString(R.string.description_with_argument, itemToEdit.itemDescription)
        }
        binding.descriptionCategory.text = getString(R.string.category_with_argument, itemToEdit.itemCategory)
        if (itemToEdit.isBought) {
            binding.descriptionBought.text = getString(R.string.bought_with_argument, "Yes")
        } else {
            binding.descriptionBought.text = getString(R.string.bought_with_argument, "No")
        }

        dialogBuilder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
        }

        return dialogBuilder.create()
    }

}