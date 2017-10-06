package com.kotlintesting.basic

/**
 * @author Amanpal Singh.
 */

class Demo2(firstName: String) {

    var firstName: String = firstName

    lateinit var list: ArrayList<Address>

    init {
        println("First Name : ${this.firstName}")
        println("First Name: $firstName")
    }

    fun addData() {
        list = ArrayList()
        for (i in 1..10) {
            var model = Address()
            model.name = "Amanpal $i"
            list.add(model)

        }

    }

    fun showList() {
        for (obj: Address in list) {
            println("name ${obj.name}")
        }
    }

}
