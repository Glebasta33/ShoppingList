package com.example.shoppinglist.di

import com.example.shoppinglist.presentation.activity.MainActivity
import com.example.shoppinglist.presentation.fragment.ShopItemFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}