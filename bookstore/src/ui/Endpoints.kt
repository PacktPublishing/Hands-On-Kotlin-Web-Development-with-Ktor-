package com.example.ui

enum class Endpoints(val url: String) {
    LOGIN("/html/login"),
    LOGOUT("/html/logout"),
    DOLOGIN("/html/dologin"),
    HOME("/html/home"),
    BOOKS("/html/books"),
    DOBOOKSEARCH("/html/books/search"),
    DOADDTOCART("/html/cart/add"),
    DOREMOVEFROMCART("/html/cart/remove"),
    CART("/html/cart"),
    RECEIPT("/html/checkout")
}