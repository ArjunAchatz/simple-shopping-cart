package com.phoenix.shoppingcart.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.phoenix.shoppingcart.MainActivity
import com.phoenix.shoppingcart.R
import com.phoenix.shoppingcart.db.IStoreDatabase
import com.phoenix.shoppingcart.db.StoreDatabase
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import java.sql.SQLException

class DetailsActivity : AppCompatActivity() {

    private val dbHelper by inject<IStoreDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val bundle = intent.extras
        val name = findViewById<View>(R.id.name) as TextView
        val description = findViewById<View>(R.id.description) as TextView
        val price = findViewById<View>(R.id.price) as TextView
        assert(name != null)
        name.text = bundle!!.getString("name")
        assert(description != null)
        description.text = bundle.getString("description")
        val priceVal = BigDecimal.valueOf(
            bundle.getInt("price").toLong(),
            2
        ) // we had stored price as a whole integer to include cents e.g 1.00 was stored as 100
        assert(price != null)
        price.text = "Price: $$priceVal"

        try {
            dbHelper.open()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        val button =
            (findViewById<View>(R.id.buy) as Button)
        button.setOnClickListener {
            if (dbHelper.addToCart(bundle.getInt("_id"), "1")) {
                val intent = Intent(this@DetailsActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Toast.makeText(
                    this@DetailsActivity,
                    "Successfully added to shopping cart",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@DetailsActivity,
                    "Oops! Something went wrong. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}