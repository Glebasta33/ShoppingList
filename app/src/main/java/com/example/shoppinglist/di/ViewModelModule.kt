package com.example.shoppinglist.di

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.presentation.viewmodel.MainViewModel
import com.example.shoppinglist.presentation.viewmodel.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindsMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    fun bindsShopItemViewModel(viewModel: ShopItemViewModel): ViewModel
}