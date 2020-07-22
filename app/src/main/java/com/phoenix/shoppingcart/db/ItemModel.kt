package com.phoenix.shoppingcart.db

data class ItemModel(
    var name: String,
    var description: String,
    var price: String,// we had stored price as a whole integer to include cents e.g 1.00 was stored as 100
    var status: String
)