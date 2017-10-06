package com.kotlintesting.basic

/**
 * @author Amanpal Singh.
 */

class Demo(num:Int) {

    var nullValue: String? = null
    var value: String = ""


    constructor(value: String) :this(0){
        this.value = value;
    }

    fun show() {
        println(value)
    }

    fun showValue(): String {
        return value
    }

}

