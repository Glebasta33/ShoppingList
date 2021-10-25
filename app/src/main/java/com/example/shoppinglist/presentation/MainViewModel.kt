package com.example.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*

class MainViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    fun addShopItem(item: ShopItem) {
        addShopItemUseCase.addShopItem(item)
    }

    val shopList = MutableLiveData<List<ShopItem>>()
    fun getShopList() {
        shopList.value = getShopListUseCase.getShopList()
    }

    fun deleteShopItem(item: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(item)
        getShopList()
    }

    fun changeEnableState(item: ShopItem) {
        val newItem = item.copy(enabled = !item.enabled);
        editShopItemUseCase.editShopItem(newItem)
        getShopList()
    }
}