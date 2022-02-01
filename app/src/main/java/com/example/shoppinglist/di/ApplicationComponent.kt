package com.example.shoppinglist.di

import android.app.Application
import com.example.shoppinglist.presentation.activity.MainActivity
import com.example.shoppinglist.presentation.fragment.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(instance: MainActivity)
    fun inject(instance: ShopItemFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            application: Application
        ): ApplicationComponent
    }
}