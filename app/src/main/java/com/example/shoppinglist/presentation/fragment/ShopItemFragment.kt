package com.example.shoppinglist.presentation.fragment

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.ShoppingListApp
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.di.ViewModelFactory
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.viewmodel.ShopItemViewModel
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    override fun onAttach(context: Context) {
        (activity?.application as ShoppingListApp).component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity $context must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
        binding.shopItemViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        launchScreenMode()
        addTextChangeListeners()
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilName.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilCount.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchScreenMode() {
        when (screenMode) {
            EDIT_MODE -> launchEditMode()
            ADD_MODE -> launchAddMode()
            else -> throw RuntimeException("Launch mode: $screenMode")
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
            viewModel.editShopItem(name, count)
            closeShopItemActivity()
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
//            viewModel.addShopItem(name, count)
            thread {
                context?.contentResolver?.insert(
                    Uri.parse("content://com.example.shoppinglist/shop_items"),
                    ContentValues().apply {
                        put("id", 0)
                        put("name", name)
                        put("count", count.toInt())
                        put("enabled", true)
                    }
                )
            }
            closeShopItemActivity()
        }
    }

    private fun closeShopItemActivity() {
        viewModel.activityIsReadyToClose.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw java.lang.RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != EDIT_MODE && mode != ADD_MODE) {
            throw java.lang.RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == EDIT_MODE) {
            if (!args.containsKey(ITEM_ID)) {
                throw java.lang.RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(ITEM_ID)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "SHOP_ITEM_SCREEN_MODE"
        private const val ADD_MODE = "ADD_MODE"
        private const val EDIT_MODE = "EDIT_MODE"
        private const val UNKNOWN_MODE = ""
        private const val ITEM_ID = "ITEM_ID"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, ADD_MODE)
                }
            }
        }

        fun newInstanceEditItem(id: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, EDIT_MODE)
                    putInt(ITEM_ID, id)
                }
            }
        }
    }
}