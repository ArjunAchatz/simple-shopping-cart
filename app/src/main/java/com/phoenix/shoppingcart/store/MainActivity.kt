package com.phoenix.shoppingcart.store

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.phoenix.shoppingcart.R
import com.phoenix.shoppingcart.db.IStoreDatabase
import com.phoenix.shoppingcart.db.StoreDatabase
import com.phoenix.shoppingcart.details.DetailsActivity
import com.phoenix.shoppingcart.shoppingcart.ShoppingCartActivity
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import java.sql.SQLException

class MainActivity : AppCompatActivity() {

    private val dbHelper by inject<IStoreDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            dbHelper.open()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        //check if items are available
        val total: Int = dbHelper.totalItemsCount
        if (total <= 0) {
            //Add some data
            dbHelper.insertMyShopItems()
        }
        //Generate ListView from SQLite Database
        displayListView()
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
        cart.setOnClickListener {
            val intent = Intent(this@MainActivity, ShoppingCartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayListView() {
        val cursor =
            dbHelper.fetchAllItems("0") // 0 is used to denote an item yet to be bought

        // Display name of item to be sold
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
        listView.onItemClickListener =
            OnItemClickListener { listView, view, position, id -> // Get the cursor, positioned to the corresponding row in the result set
                val cursor =
                    listView.getItemAtPosition(position) as Cursor

                // Get the item attributes to be sent to details activity from this row in the database.
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val description =
                    cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                val itemId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("description", description)
                intent.putExtra("price", price)
                intent.putExtra("_id", itemId)
                startActivity(intent)
            }
    }
}