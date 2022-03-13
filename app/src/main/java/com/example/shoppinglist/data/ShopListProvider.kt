package com.example.shoppinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.shoppinglist.ShoppingListApp
import com.example.shoppinglist.domain.ShopItem
import javax.inject.Inject

class ShopListProvider : ContentProvider() {

    private val component by lazy {
        (context as ShoppingListApp).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao
    @Inject
    lateinit var mapper: ShopListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.example.shoppinglist", "shop_items", GET_SHOP_ITEMS_QUERY)
        addURI("com.example.shoppinglist", "shop_items/*", GET_SHOP_ITEM_BY_ID_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> shopListDao.getShopListCursor()
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                contentValues?.let {
                    val id = it.getAsInteger("id")
                    val name = it.getAsString("name")
                    val count = it.getAsInteger("count")
                    val enabled = it.getAsBoolean("enabled")

                    val shopItem = ShopItem(name, count, enabled, id)
                    shopListDao.putShopItem(mapper.mapEntityToDbModel(shopItem))
                }
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val GET_SHOP_ITEMS_QUERY = 100
        private const val GET_SHOP_ITEM_BY_ID_QUERY = 101
    }
}