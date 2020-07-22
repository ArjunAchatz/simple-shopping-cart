package com.phoenix.shoppingcart.db

import android.database.Cursor
import android.database.SQLException

interface IStoreDatabase {

    fun createItem(
        name: String?,
        description: String?,
        price: Int,
        status: String?
    ): Long

    fun deleteAllItems(): Boolean
    fun getCartItemsRowCount(type: Int): Int
    fun addToCart(id: Int?, `val`: String?): Boolean
    val totalItemsCount: Int
    val amount: Int
    fun fetchAllItems(status: String?): Cursor?
    fun insertMyShopItems()

    // Remove later
    fun open(): IStoreDatabase
}