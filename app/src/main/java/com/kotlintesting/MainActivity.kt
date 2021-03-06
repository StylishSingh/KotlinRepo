package com.kotlintesting

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.kotlintesting.basic.Demo
import com.kotlintesting.basic.Demo2
import com.kotlintesting.fresco.FrescoActivity
import com.kotlintesting.fresco.ListPhotos
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val demo: Demo = Demo("Clicked")

    private var count: Int = 0
    private var mEmail: String = ""
    private var mPassword: String = ""


    private val submitListener = View.OnClickListener { view ->

        when (view.id) {
            R.id.fabClick -> {
                val demo2 = Demo2("Anuj")

                demo2.addData()
                demo2.showList()

                updateTextValue()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabClick.setOnClickListener(submitListener)
        btnFresco.setOnClickListener(this)

        btnSubmit.setOnClickListener {
            submitClick()
        }
/*          btnSubmit.setOnClickListener(this)
          btnSubmit.setOnClickListener(object : View.OnClickListener {
              override fun onClick(p0: View?) {
                  submitClick()
              }
          })*/

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnSubmit -> {
                submitClick()
            }

            R.id.btnFresco -> {
                startActivity(Intent(this, FrescoActivity::class.java))
//                startActivity(Intent(this, ListPhotos::class.java))
            }
        }


    }

    private fun submitClick() {
        mEmail = etEmail.text.toString()
        mPassword = etPassword.text.toString()

        println("mEmail = $mEmail")
        println("mPassword = $mPassword")

        if (mEmail.contentEquals("") && mPassword.contentEquals(""))
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
        else if (mEmail.contentEquals(""))
            Toast.makeText(this, "please enter mEmail", Toast.LENGTH_SHORT).show()
        else if (mPassword.contentEquals(""))
            Toast.makeText(this, "please enter mPassword", Toast.LENGTH_SHORT).show()
        else {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("mEmail", mEmail)
            startActivity(intent)
//                val bundle: Bundle = Bundle()
//                bundle.putString("mEmail", mEmail)
//                startActivity(intent, bundle)    }
        }
    }


    private fun updateTextValue() {
        val updateVal = "${demo.showValue()} $count times"

        tvValue.text = updateVal

        count++

        demo.show()
    }
}
