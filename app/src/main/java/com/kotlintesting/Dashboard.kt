package com.kotlintesting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    val users = ArrayList<String>()

    var mItems = arrayOf("One", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (intent.extras != null) {
            println("mEmail---${intent.getStringExtra("mEmail")}")
        } else {
            println("null intent.extra")
        }

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        //crating an arraylist to store users using the data class user

        //adding some dummy data to the list
        users.add("Item 1")
        users.add("Item 2")
        users.add("Item 3")
        users.add("Item 4")
        users.add("Item 5")
        users.add("Item 6")
        users.add("Item 7")
        users.add("Item 8")
        users.add("Item 9")
        users.add("Item 10")

        //creating our adapter
        val adapter = ItemsAdapter(users,this)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter


    }

    fun onItemClicked(adapterPosition: Int) {


        Toast.makeText(this,"${users[adapterPosition]} clicked",Toast.LENGTH_SHORT).show()
    }
}
