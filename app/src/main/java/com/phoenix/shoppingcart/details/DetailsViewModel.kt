package com.phoenix.shoppingcart.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phoenix.shoppingcart.db.IStoreDatabase
import java.math.BigDecimal
import java.sql.SQLException

class DetailsViewModel(
    app: Application,
    private val dbHelper: IStoreDatabase
) : AndroidViewModel(app) {

    private val uiModel = MutableLiveData<DetailsUiModel>()
    private val singleEvents = MutableLiveData<DetailsSingleEvent>()

    fun initialize(title: String?, description: String?, price: Int) {
        uiModel.postValue(
            DetailsUiModel(
                title = title ?: "",
                description = description ?: "",
                price = "Price: $${BigDecimal.valueOf(price.toLong(), 2)}"
            )
        )
    }

    fun getUiModel() = uiModel as LiveData<DetailsUiModel>

    fun getSingleEvents() = singleEvents as LiveData<DetailsSingleEvent>

    fun buyClicked(id: Int) {
        dbHelper.open()
        
        if (dbHelper.addToCart(id, "1")) {
            singleEvents.postValue(DetailsSingleEvent.BuySuccessful)
            return
        }

        singleEvents.postValue(DetailsSingleEvent.BuyFailed)
    }

}