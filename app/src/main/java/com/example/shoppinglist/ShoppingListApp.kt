package com.example.shoppinglist

import android.app.Application
import com.example.shoppinglist.di.DaggerApplicationComponent

class ShoppingListApp : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }
}