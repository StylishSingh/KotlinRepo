package com.kotlintesting.basic

/**
 * @author Amanpal Singh.
 */

class Demo {

    var nullValue: String? = null
    var value: String = ""


    constructor(value: String) {
        this.value = value;
    }

    fun show() {
        println(value)
    }

    fun showValue(): String {
        return value
    }

}

