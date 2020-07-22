package com.phoenix.shoppingcart.di

import com.phoenix.shoppingcart.db.IStoreDatabase
import com.phoenix.shoppingcart.db.StoreDatabase
import com.phoenix.shoppingcart.details.DetailsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single { StoreDatabase(androidContext()) as IStoreDatabase }

    viewModel { DetailsViewModel(androidApplication(), get()) }

}