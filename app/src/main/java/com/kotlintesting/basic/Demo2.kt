package com.kotlintesting.basic

/**
 * @author Amanpal Singh.
 */

class Demo2(firstName: String) {

    var firstName: String = firstName

    init {
        println("First Name : ${this.firstName}")
        println("First Name: ${firstName}")
    }

}
