package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {

    private val liveData = MutableLiveData<List<ShopItem>>();
    private var shopList = sortedSetOf<ShopItem>({ p0, p1 -> p0.id.compareTo(p1.id) });
    private var autoIncrement = 0

    init {
        for (i in 0 until 20) {
            addShopItem(ShopItem("Item №$i", i, true))
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrement++
        }
        shopList.add(shopItem)
        updateLiveData()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLiveData()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find {
            it.id == id
        } ?: throw RuntimeException("Element with id $id not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return liveData
    }

    private fun updateLiveData() {
        liveData.value = shopList.toList()
    }
}