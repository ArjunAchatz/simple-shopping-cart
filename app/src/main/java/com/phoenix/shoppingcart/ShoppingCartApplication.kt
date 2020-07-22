package com.phoenix.shoppingcart

import android.app.Application
import com.phoenix.shoppingcart.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShoppingCartApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShoppingCartApplication)
            modules(appModules)
        }
    }

}