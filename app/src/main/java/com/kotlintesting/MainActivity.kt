package com.kotlintesting

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val submitListener = View.OnClickListener { view ->

        when (view.id) {
            R.id.btnSubmit -> {
                submitClick()
            }
        }

    }

    var mEmail: String = ""
    var mPassword: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSubmit.setOnClickListener(submitListener)

        /* btnSubmit.setOnClickListener(this)
          btnSubmit.setOnClickListener {
              submitClick()
          }

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
        }


    }

    fun submitClick() {
        mEmail = etEmail.text.toString()
        mPassword = etPassword.text.toString()

        println("mEmail = ${mEmail}")
        println("mPassword = $mPassword")

        if (mEmail.contentEquals("") && mPassword.contentEquals(""))
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
        else if (mEmail.contentEquals(""))
            Toast.makeText(this, "please enter mEmail", Toast.LENGTH_SHORT).show()
        else if (mPassword.contentEquals(""))
            Toast.makeText(this, "please enter mPassword", Toast.LENGTH_SHORT).show()
        else {
            val intent: Intent = Intent(this, Dashboard::class.java)
            intent.putExtra("mEmail", mEmail)
            startActivity(intent)
//                val bundle: Bundle = Bundle()
//                bundle.putString("mEmail", mEmail)
//                startActivity(intent, bundle)    }
        }
    }
}