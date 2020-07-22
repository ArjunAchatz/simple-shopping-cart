package com.phoenix.shoppingcart.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.phoenix.shoppingcart.store.MainActivity
import com.phoenix.shoppingcart.databinding.ActivityDetailsBinding
import com.phoenix.shoppingcart.db.IStoreDatabase
import com.phoenix.shoppingcart.util.exhaust
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.IllegalStateException
import java.sql.SQLException

class DetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<DetailsViewModel>()

    private val dbHelper by inject<IStoreDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding = ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        val bundle = intent.extras ?: throw IllegalStateException(
            "Developer mistake: This activity needs data passed into it"
        )

        viewModel.getUiModel().observe(this, Observer { renderUiModel(it, viewBinding) })
        viewModel.getSingleEvents().observe(this, Observer { handleSingleEvent(it, viewBinding) })
        viewModel.initialize(
            bundle.getString("name"),
            bundle.getString("description"),
            bundle.getInt("price")
        )

        viewBinding.buy.setOnClickListener { viewModel.buyClicked(bundle.getInt("_id")) }
    }

    private fun renderUiModel(
        detailsUiModel: DetailsUiModel,
        viewBinding: ActivityDetailsBinding
    ) {
        viewBinding.name.text = detailsUiModel.title
        viewBinding.description.text = detailsUiModel.description
        viewBinding.price.text = detailsUiModel.price
    }

    private fun handleSingleEvent(
        detailsSingleEvent: DetailsSingleEvent,
        viewBinding: ActivityDetailsBinding
    ) {
        when (detailsSingleEvent) {
            DetailsSingleEvent.BuySuccessful -> {
                val intent = Intent(this@DetailsActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Toast.makeText(
                    this@DetailsActivity,
                    "Successfully added to shopping cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
            DetailsSingleEvent.BuyFailed -> {
                Toast.makeText(
                    this@DetailsActivity,
                    "Oops! Something went wrong. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.exhaust()
    }
}
