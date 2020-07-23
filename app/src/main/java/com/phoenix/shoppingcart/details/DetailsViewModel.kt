package com.phoenix.shoppingcart.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phoenix.shoppingcart.db.IStoreDatabase
import com.phoenix.shoppingcart.util.SingleLiveEvent
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal

class DetailsViewModel(
    app: Application,
    private val dbHelper: IStoreDatabase
) : AndroidViewModel(app) {

    private val actions = ConflatedBroadcastChannel<Actions>()
    private val uiModel = MutableLiveData<DetailsUiModel>()
    private val singleEvents = SingleLiveEvent<DetailsSingleEvent>()

    init {
        viewModelScope.launch {
            actions
                .asFlow()
                .scan(DetailsUiModel()) { uiModel, action ->
                    return@scan when (action) {
                        is Actions.Initialized -> uiModel.copy(
                            title = action.title ?: "",
                            description = action.description ?: "",
                            price = "Price: $${BigDecimal.valueOf(action.price.toLong(), 2)}"
                        )
                    }
                }
                .collect { uiModel.postValue(it) }
        }
    }

    fun initialize(title: String?, description: String?, price: Int) {
        actions.offer(Actions.Initialized(title, description, price))
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

    private sealed class Actions {
        class Initialized(val title: String?, val description: String?, val price: Int) : Actions()
    }

}