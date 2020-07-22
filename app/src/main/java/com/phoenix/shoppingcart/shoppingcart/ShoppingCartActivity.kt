package com.phoenix.shoppingcart.shoppingcart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.phoenix.shoppingcart.MainActivity
import com.phoenix.shoppingcart.R
import com.phoenix.shoppingcart.db.IStoreDatabase
import com.phoenix.shoppingcart.db.StoreDatabase
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import java.sql.SQLException

class ShoppingCartActivity : AppCompatActivity() {

    private val dbHelper by inject<IStoreDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        try {
            dbHelper.open()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        val total: Int = dbHelper.totalItemsCount
        val num = dbHelper.getCartItemsRowCount(1)
        val amount: Int = dbHelper.amount
        val priceVal: BigDecimal
        priceVal = if (total == num) {
            val tAmount = amount - 0.2 * amount
            BigDecimal.valueOf(tAmount.toLong(), 2)
        } else {
            BigDecimal.valueOf(amount.toLong(), 2)
        }
        val numItemsBought = findViewById<View>(R.id.cart) as TextView
        numItemsBought.text = "$num of $total items"
        val totalAmount = findViewById<View>(R.id.total) as TextView
        totalAmount.text = "Total Amount: $$priceVal"
        val cart =
            (findViewById<View>(R.id.linearLayout) as LinearLayout)
        cart.setOnClickListener { //Clean all data
            dbHelper.deleteAllItems()
            val intent = Intent(this@ShoppingCartActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        //Generate ListView from SQLite Database
        displayListView()
    }

    private fun displayListView() {
        val cursor =
            dbHelper.fetchAllItems("1") // 1 is used to denote an item in the shopping cart

        // Display name of item to be bought
        val columns = arrayOf(
            StoreDatabase.KEY_NAME
        )

        // the XML defined view which the data will be bound to
        val to = intArrayOf(
            R.id.name
        )

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        val dataAdapter = SimpleCursorAdapter(
            this, R.layout.item_layout,
            cursor,
            columns,
            to,
            0
        )
        val listView =
            (findViewById<View>(R.id.listView) as ListView)
        listView.adapter = dataAdapter
    }
}