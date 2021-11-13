package com.example.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("inputNameErrorMessage")
fun bindInputNameErrorMessage(input: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        input.context.getString(R.string.inputNameErrorMessage)
    } else {
        null
    }
    input.error = message
}

@BindingAdapter("inputCountErrorMessage")
fun bindInputCountErrorMessage(input: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        input.context.getString(R.string.inputCountErrorMessage)
    } else {
        null
    }
    input.error = message
}

