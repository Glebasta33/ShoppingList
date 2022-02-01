package com.example.shoppinglist.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.fragment.ShopItemFragment
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseScreenMode()
        if (savedInstanceState == null) {
            launchScreenMode()
        }
    }

    private fun launchScreenMode() {
        val fragment = when (screenMode) {
            ADD_MODE -> ShopItemFragment.newInstanceAddItem()
            EDIT_MODE -> ShopItemFragment.newInstanceEditItem(shopItemId)
            else -> throw RuntimeException("Launch mode: $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseScreenMode() {
        if (!intent.hasExtra(ACTIVITY_MODE)) {
            throw RuntimeException("Param activity mode is absent")
        }
        val mode = intent.getStringExtra(ACTIVITY_MODE)
        if (mode != EDIT_MODE && mode != ADD_MODE) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == EDIT_MODE) {
            if (!intent.hasExtra(ITEM_ID)) {
                    throw RuntimeException("Param shop item id is absent")
                }
            shopItemId = intent.getIntExtra(ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val ACTIVITY_MODE = "SHOP_ITEM_ACTIVITY_MODE"
        private const val ADD_MODE = "ADD_MODE"
        private const val EDIT_MODE = "EDIT_MODE"
        private const val UNKNOWN_MODE = ""
        private const val ITEM_ID = "ITEM_ID"

        fun newIntentAddMode(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, ADD_MODE)
            return intent
        }

        fun newIntentEditMode(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, EDIT_MODE)
            intent.putExtra(ITEM_ID, itemId)
            return intent
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@ShopItemActivity, "Success", Toast.LENGTH_SHORT).show()
        finish()
    }
}