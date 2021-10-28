package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.R

class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        val mode = intent.getStringExtra(ACTIVITY_MODE)
        Toast.makeText(this, "$mode. ID: $", Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val ACTIVITY_MODE = "SHOP_ITEM_ACTIVITY_MODE"
        private const val ADD_MODE = "ADD_MODE"
        private const val EDIT_MODE = "EDIT_MODE"
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
}