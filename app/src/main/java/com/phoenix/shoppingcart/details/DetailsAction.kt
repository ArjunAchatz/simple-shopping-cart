package com.phoenix.shoppingcart.details

sealed class DetailsAction {

    class BuyClicked(val id: Int) : DetailsAction()

}