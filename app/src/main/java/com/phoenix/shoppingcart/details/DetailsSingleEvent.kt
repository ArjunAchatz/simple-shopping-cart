package com.phoenix.shoppingcart.details

sealed class DetailsSingleEvent {

    object BuySuccessful : DetailsSingleEvent()
    
    object BuyFailed : DetailsSingleEvent()

}