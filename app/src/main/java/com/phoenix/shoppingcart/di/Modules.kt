package com.phoenix.shoppingcart.di

import com.phoenix.shoppingcart.db.IStoreDatabase
import com.phoenix.shoppingcart.db.StoreDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {

    single { StoreDatabase(androidContext()) as IStoreDatabase }

}